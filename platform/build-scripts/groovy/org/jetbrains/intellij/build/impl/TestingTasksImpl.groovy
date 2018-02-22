// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.intellij.build.impl

import com.intellij.execution.CommandLineWrapperUtil
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtilRt
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.apache.tools.ant.AntClassLoader
import org.apache.tools.ant.types.Path
import org.jetbrains.intellij.build.CompilationContext
import org.jetbrains.intellij.build.CompilationTasks
import org.jetbrains.intellij.build.TestingOptions
import org.jetbrains.intellij.build.TestingTasks
import org.jetbrains.jps.model.library.JpsLibrary
import org.jetbrains.jps.model.library.JpsOrderRootType
import org.jetbrains.jps.model.module.JpsModule
import org.jetbrains.jps.util.JpsPathUtil

import java.util.function.Predicate
import java.util.jar.Manifest

/**
 * @author nik
 */
@CompileStatic
class TestingTasksImpl extends TestingTasks {
  private final CompilationContext context
  private final TestingOptions options

  TestingTasksImpl(CompilationContext context, TestingOptions options) {
    this.options = options
    this.context = context
  }

  @Override
  void runTests(List<String> additionalJvmOptions, String defaultMainModule, Predicate<File> rootExcludeCondition) {
    checkOptions()

    def compilationTasks = CompilationTasks.create(context)
    def runConfigurations = options.testConfigurations?.split(";")?.collect { String name ->
      JUnitRunConfigurationProperties.findRunConfiguration(context.paths.projectHome, name, context.messages)
    }
    if (runConfigurations != null) {
      compilationTasks.compileModules(["intellij.tools.testsBootstrap"], ["intellij.platform.buildScripts"] + runConfigurations.collect { it.moduleName })
      compilationTasks.buildProjectArtifacts(runConfigurations.collectMany {it.requiredArtifacts})
    }
    else if (options.mainModule != null) {
      compilationTasks.compileModules(["intellij.tools.testsBootstrap"], [options.mainModule, "intellij.platform.buildScripts"])
    }
    else {
      compilationTasks.compileAllModulesAndTests()
    }

    setupTestingDependencies()

    def remoteDebugJvmOptions = System.getProperty("teamcity.remote-debug.jvm.options")
    if (remoteDebugJvmOptions != null) {
      debugTests(remoteDebugJvmOptions, additionalJvmOptions, defaultMainModule, rootExcludeCondition)
    }
    else {
      Map<String, String> additionalSystemProperties = [:]
      loadTestDiscovery(additionalJvmOptions, additionalSystemProperties)

      if (runConfigurations != null) {
        runTestsFromRunConfigurations(additionalJvmOptions, runConfigurations, additionalSystemProperties)
      }
      else {
        runTestsFromGroupsAndPatterns(additionalJvmOptions, defaultMainModule, rootExcludeCondition, additionalSystemProperties)
      }
      publishTestDiscovery()
    }
  }

  private void checkOptions() {
    if (options.testConfigurations != null) {
      if (options.testPatterns != null) {
        context.messages.warning("'intellij.build.test.configurations' option is specified so 'intellij.build.test.patterns' will be ignored.")
      }
      if (options.testGroups != TestingOptions.ALL_EXCLUDE_DEFINED_GROUP) {
        context.messages.warning("'intellij.build.test.configurations' option is specified so 'intellij.build.test.groups' will be ignored.")
      }
      if (options.testConfigurations != null && options.mainModule != null) {
        context.messages.warning("'intellij.build.test.configurations' option is specified so 'intellij.build.test.main.module' will be ignored.")
      }
    }
    else if (options.testPatterns != null && options.testGroups != TestingOptions.ALL_EXCLUDE_DEFINED_GROUP) {
      context.messages.warning("'intellij.build.test.patterns' option is specified so 'intellij.build.test.groups' will be ignored.")
    }
  }

  private void runTestsFromRunConfigurations(List<String> additionalJvmOptions,
                                             List<JUnitRunConfigurationProperties> runConfigurations,
                                             Map<String, String> additionalSystemProperties) {
    runConfigurations.each { configuration ->
      context.messages.block("Run '${configuration.name}' run configuration") {
        runTestsFromRunConfiguration(configuration, additionalJvmOptions, additionalSystemProperties)
      }
    }
  }

  private void runTestsFromRunConfiguration(JUnitRunConfigurationProperties runConfigurationProperties,
                                            List<String> additionalJvmOptions,
                                            Map<String, String> additionalSystemProperties) {
    context.messages.progress("Running '${runConfigurationProperties.name}' run configuration")
    List<String> filteredVmOptions = removeStandardJvmOptions(runConfigurationProperties.vmParameters)
    runTestsProcess(runConfigurationProperties.moduleName, null, runConfigurationProperties.testClassPatterns.join(";"),
                    filteredVmOptions + additionalJvmOptions, additionalSystemProperties, runConfigurationProperties.envVariables, false)
  }

  private static List<String> removeStandardJvmOptions(List<String> vmOptions) {
    def ignoredPrefixes = [
      "-ea", "-XX:+HeapDumpOnOutOfMemoryError",
      "-Xbootclasspath",
      "-Xmx", "-Xms",
      "-Didea.system.path=", "-Didea.config.path=", "-Didea.home.path="
    ]
    vmOptions.findAll { option -> ignoredPrefixes.every { !option.startsWith(it) } }
  }

  private void runTestsFromGroupsAndPatterns(List<String> additionalJvmOptions,
                                             String defaultMainModule,
                                             Predicate<File> rootExcludeCondition,
                                             Map<String, String> additionalSystemProperties) {
    def mainModule = options.mainModule ?: defaultMainModule
    if (rootExcludeCondition != null) {
      List<JpsModule> excludedModules = context.project.modules.findAll {
        List<String> contentRoots = it.contentRootsList.urls
        !contentRoots.isEmpty() && rootExcludeCondition.test(JpsPathUtil.urlToFile(contentRoots.first()))
      }
      List<String> excludedRoots = excludedModules.collectMany {
        [context.getModuleOutputPath(it), context.getModuleTestsOutputPath(it)]
      }
      File excludedRootsFile = new File("$context.paths.temp/excluded.classpath")
      FileUtilRt.createParentDirs(excludedRootsFile)
      excludedRootsFile.text = excludedRoots.findAll { new File(it).exists() }.join('\n')
      additionalSystemProperties["exclude.tests.roots.file"] = excludedRootsFile.absolutePath
    }

    runTestsProcess(mainModule, options.testGroups, options.testPatterns, additionalJvmOptions, additionalSystemProperties, [:], false)
  }

  private loadTestDiscovery(List<String> additionalJvmOptions, LinkedHashMap<String, String> additionalSystemProperties) {
    if (options.testDiscoveryEnabled) {
      def testDiscovery = "intellij-test-discovery"
      JpsLibrary library = context.projectModel.project.libraryCollection.findLibrary(testDiscovery)
      if (library == null) context.messages.error("Can't find the $testDiscovery library, but test discovery capturing enabled.")
      def agentJar = library.getFiles(JpsOrderRootType.COMPILED).find { it.name.startsWith("intellij-test-discovery") && it.name.endsWith(".jar") }
      if (agentJar == null) context.messages.error("Can't find the agent in $testDiscovery library, but test discovery capturing enabled.")

      additionalJvmOptions.add("-javaagent:${agentJar.absolutePath}" as String)
      additionalSystemProperties.putAll(
        [
          "test.discovery.listener"                 : "com.intellij.TestDiscoveryBasicListener",
          "test.discovery.data.listener"            : "com.intellij.rt.coverage.data.SingleTrFileDiscoveryProtocolDataListener",
          "org.jetbrains.instrumentation.trace.file": getTestDiscoveryTraceFilePath(),
          "test.discovery.include.class.patterns"   : options.testDiscoveryIncludePatterns,
          "test.discovery.exclude.class.patterns"   : options.testDiscoveryExcludePatterns,
        ] as Map<String, String>)
    }
  }

  private String getTestDiscoveryTraceFilePath() {
    options.testDiscoveryTraceFilePath ?: "${context.paths.projectHome}/intellij-tracing/td.tr"
  }

  private publishTestDiscovery() {
    if (options.testDiscoveryEnabled) {
      def file = getTestDiscoveryTraceFilePath()
      def serverUrl = System.getProperty("intellij.test.discovery.url")
      context.messages.info("Trying to upload $file into $serverUrl.")
      if (file != null && new File(file).exists()) {
        if (serverUrl == null) {
          context.messages.warning("Test discovery server url is not defined, but test discovery capturing enabled. \n" +
                                   "Will not upload to remote server. Please set 'intellij.test.discovery.url' system property.")
          return
        }
        def uploader = new TraceFileUploader(serverUrl) {
          @Override
          protected void log(String message) {
            context.messages.info(message)
          }
        }
        try {
          uploader.upload(new File(file), [
            'teamcity-build-number'            : System.getProperty('build.number'),
            'teamcity-build-type-id'           : System.getProperty('teamcity.buildType.id'),
            'teamcity-build-configuration-name': System.getenv('TEAMCITY_BUILDCONF_NAME'),
            'teamcity-build-project-name'      : System.getenv('TEAMCITY_PROJECT_NAME'),
          ])
        }
        catch (Exception e) {
          context.messages.error(e.message, e)
        }
      }
    }
  }

  private void debugTests(String remoteDebugJvmOptions, List<String> additionalJvmOptions, String defaultMainModule, Predicate<File> rootExcludeCondition) {
    def testConfigurationType = System.getProperty("teamcity.remote-debug.type")
    if (testConfigurationType != "junit") {
      context.messages.error("Remote debugging is supported for junit run configurations only, but 'teamcity.remote-debug.type' is $testConfigurationType")
    }

    def testObject = System.getProperty("teamcity.remote-debug.junit.type")
    def junitClass = System.getProperty("teamcity.remote-debug.junit.class")
    if (testObject != "class") {
      context.messages.error("Remote debugging supports debugging all test methods in a class for now, debugging isn't supported for '$testObject'")
    }
    if (junitClass == null) {
      context.messages.error("Remote debugging supports debugging all test methods in a class for now, but target class isn't specified")
    }
    if (options.testPatterns != null) {
      context.messages.warning("'intellij.build.test.patterns' option is ignored while debugging via TeamCity plugin")
    }
    if (options.testConfigurations != null) {
      context.messages.warning("'intellij.build.test.configurations' option is ignored while debugging via TeamCity plugin")
    }
    def mainModule = options.mainModule ?: defaultMainModule
    def filteredOptions = removeStandardJvmOptions(remoteDebugJvmOptions.split(";").toList())
    runTestsProcess(mainModule, null, junitClass, filteredOptions + additionalJvmOptions, [:], [:], true)
  }

  private void runTestsProcess(String mainModule, String testGroups, String testPatterns,
                               List<String> additionalJvmOptions, Map<String, String> additionalSystemProperties, Map<String, String> envVariables, boolean remoteDebugging) {
    List<String> testsClasspath = context.getModuleRuntimeClasspath(context.findRequiredModule(mainModule), true)
    List<String> bootstrapClasspath = context.getModuleRuntimeClasspath(context.findRequiredModule("intellij.tools.testsBootstrap"), false)
    def classpathFile = new File("$context.paths.temp/junit.classpath")
    FileUtilRt.createParentDirs(classpathFile)
    classpathFile.text = testsClasspath.findAll({ new File(it).exists() }).join('\n')

    File snapshotsDir = createSnapshotsDirectory()
    String hprofSnapshotFilePath = new File(snapshotsDir, "intellij-tests-oom.hprof").absolutePath
    List<String> jvmArgs = [
      "-ea",
      "-server",
      "-XX:+HeapDumpOnOutOfMemoryError",
      "-XX:HeapDumpPath=$hprofSnapshotFilePath".toString(),
      "-XX:ReservedCodeCacheSize=300m",
      "-XX:SoftRefLRUPolicyMSPerMB=50",
      "-XX:+UseConcMarkSweepGC",
      "-XX:-OmitStackTraceInFastThrow"
    ]
    jvmArgs.addAll(additionalJvmOptions)
    if (options.jvmMemoryOptions != null) {
      jvmArgs.addAll(options.jvmMemoryOptions.split())
    }
    else {
      jvmArgs.addAll([
        "-Xmx750m",
        "-Xms750m",
        "-Dsun.io.useCanonCaches=false"
      ])
    }

    String tempDir = System.getProperty("teamcity.build.tempDir", System.getProperty("java.io.tmpdir"))

    Map<String, String> systemProperties = [
      "classpath.file"                         : classpathFile.absolutePath,
      "idea.platform.prefix"                   : options.platformPrefix,
      "idea.home.path"                         : context.paths.projectHome,
      "idea.config.path"                       : "$tempDir/config".toString(),
      "idea.system.path"                       : "$tempDir/system".toString(),
      "intellij.build.test.patterns"           : testPatterns,
      "intellij.build.test.groups"             : testGroups,
      "idea.performance.tests"                 : System.getProperty("idea.performance.tests"),
      "idea.coverage.enabled.build"            : System.getProperty("idea.coverage.enabled.build"),
      "teamcity.buildConfName"                 : System.getProperty("teamcity.buildConfName"),
      "bootstrap.testcases"                    : "com.intellij.AllTests",
      "java.io.tmpdir"                         : tempDir,
      "teamcity.build.tempDir"                 : tempDir,
      "teamcity.tests.recentlyFailedTests.file": System.getProperty("teamcity.tests.recentlyFailedTests.file"),
      "jna.nosys"                              : "true",
      "file.encoding"                          : "UTF-8",
      "io.netty.leakDetectionLevel"            : "PARANOID",
    ] as Map<String, String>
    systemProperties.putAll(additionalSystemProperties)

    (System.getProperties() as Hashtable<String, String>).each { String key, String value ->
      if (key.startsWith("pass.")) {
        systemProperties[key.substring("pass.".length())] = value
      }
    }

    boolean suspendDebugProcess = options.suspendDebugProcess
    if (systemProperties["idea.performance.tests"] == "true") {
      context.messages.info("Debugging disabled for performance tests")
      suspendDebugProcess = false
    }
    else if (remoteDebugging) {
      context.messages.info("Remote debugging via TeamCity plugin is activated.")
      if (suspendDebugProcess) {
        context.messages.warning("'intellij.build.test.debug.suspend' option is ignored while debugging via TeamCity plugin")
        suspendDebugProcess = false
      }
    }
    else {
      String debuggerParameter = "-agentlib:jdwp=transport=dt_socket,server=y,suspend=${suspendDebugProcess ? "y" : "n"}"
      if (options.debugPort != -1) {
        debuggerParameter += ",address=$options.debugPort"
      }
      jvmArgs.add(debuggerParameter)
    }

    context.messages.info("Starting ${testGroups != null ? "test from groups '${testGroups}'" : "all tests"}")
    if (options.customJrePath != null) {
      context.messages.info("JVM: $options.customJrePath")
    }
    context.messages.info("JVM options: $jvmArgs")
    context.messages.info("System properties: $systemProperties")
    context.messages.info("Bootstrap classpath: $bootstrapClasspath")
    context.messages.info("Tests classpath: $testsClasspath")
    if (!envVariables.isEmpty()) {
      context.messages.info("Environment variables: $envVariables")
    }

    if (suspendDebugProcess) {
      context.messages.info("""
------------->------------- The process suspended until remote debugger connects to debug port -------------<-------------
---------------------------------------^------^------^------^------^------^------^----------------------------------------
""")
    }
    if (isBootstrapSuiteDefault()) {
      runJUnitTask(jvmArgs, systemProperties, envVariables, bootstrapClasspath)
    }
    else {
      //run other suites instead of BootstrapTests
      runJUnitTask(jvmArgs, systemProperties, envVariables, testsClasspath)
    }

    if (new File(hprofSnapshotFilePath).exists()) {
      context.notifyArtifactBuilt(hprofSnapshotFilePath)
    }
  }

  @Override
  @CompileDynamic
  File createSnapshotsDirectory() {
    File snapshotsDir = new File("$context.paths.projectHome/out/snapshots")
    context.ant.delete(dir: snapshotsDir)
    context.ant.mkdir(dir: snapshotsDir)
    return snapshotsDir
  }

  @SuppressWarnings("GrUnresolvedAccess")
  @CompileDynamic
  private void runJUnitTask(List<String> jvmArgs, Map<String, String> systemProperties, Map<String, String> envVariables, List<String> bootstrapClasspath) {
    defineJunitTask(context.ant, "$context.paths.communityHome/lib")

    String junitTemp = "$context.paths.temp/junit"
    context.ant.mkdir(dir: junitTemp)

    List<String> teamCityFormatterClasspath = createTeamCityFormatterClasspath()

    String jvmExecutablePath = options.customJrePath != null ? "$options.customJrePath/bin/java" : ""
    context.ant.junit(fork: true, showoutput: true, logfailedtests: false, tempdir: junitTemp, jvm: jvmExecutablePath, printsummary: (underTeamCity ? "off" : "on")) {
      jvmArgs.each { jvmarg(value: it) }
      systemProperties.each { key, value ->
        if (value != null) {
          sysproperty(key: key, value: value)
        }
      }
      envVariables.each {
        env(key: it.key, value: it.value)
      }

      if (teamCityFormatterClasspath != null) {
        classpath {
          teamCityFormatterClasspath.each {
            pathelement(location: it)
          }
        }
        formatter(classname: "jetbrains.buildServer.ant.junit.AntJUnitFormatter3", usefile: false)
        context.messages.info("Added TeamCity's formatter to JUnit task")
      }
      if (!underTeamCity) {
        classpath {
          pathelement(location: context.getModuleTestsOutputPath(context.findRequiredModule("intellij.platform.buildScripts")))
        }
        formatter(classname: "org.jetbrains.intellij.build.JUnitLiveTestProgressFormatter", usefile: false)
      }

      //if it is Windows OS, test classpath may exceed the maximum command line, so we need to wrap a classpath in a jar
      if (SystemInfo.isWindows && !isBootstrapSuiteDefault()) {
        def classpathJarFile = CommandLineWrapperUtil.createClasspathJarFile(new Manifest(), bootstrapClasspath)
        classpath {
          pathelement(location: classpathJarFile.path)
        }
      } else {
        classpath {
          bootstrapClasspath.each {
            pathelement(location: it)
          }
        }
      }

      test(name: options.bootstrapSuite)
    }
  }

  /**
   * In simple cases when JUnit tests are started from Ant scripts TeamCity will automatically add its formatter to JUnit task. However it
   * doesn't work if JUnit task is called from Groovy code via a new instance of AntBuilder, so in such cases we need to add the formatter
   * explicitly.
   * @return classpath for TeamCity's JUnit formatter or {@code null} if the formatter shouldn't be added
   */
  private List<String> createTeamCityFormatterClasspath() {
    if (!underTeamCity) return null

    if (context.ant.project.buildListeners.any { it.class.name.startsWith("jetbrains.buildServer.") }) {
      context.messages.info("TeamCity's BuildListener is registered in the Ant project so its formatter will be added to JUnit task automatically.")
      return null
    }

    String agentHomeDir = System.getProperty("agent.home.dir")
    if (agentHomeDir == null) {
      context.messages.error("'agent.home.dir' system property isn't set, cannot add TeamCity JARs to classpath.")
    }
    List<String> classpath = [
      "$agentHomeDir/lib/runtime-util.jar",
      "$agentHomeDir/lib/serviceMessages.jar",
      "$agentHomeDir/plugins/antPlugin/ant-runtime.jar",
      "$agentHomeDir/plugins/junitPlugin/junit-runtime.jar",
      "$agentHomeDir/plugins/junitPlugin/junit-support.jar"
    ].collect {it.toString()}
    classpath.each {
      if (!new File(it).exists()) {
        context.messages.error("Cannot add required JARs from $agentHomeDir to classpath: $it doesn't exist")
      }
    }
    return classpath
  }

  private static boolean isUnderTeamCity() {
    System.getProperty("teamcity.buildType.id") != null
  }

  static boolean dependenciesInstalled
  private def setupTestingDependencies() {
    if (!dependenciesInstalled) {
      dependenciesInstalled = true
      context.gradle.run('Setting up testing dependencies', 'setupKotlinPlugin')
    }
  }

  static boolean taskDefined

  /**
   * JUnit is an optional dependency in Ant, so by defining its tasks dynamically we simplify setup for gant/Ant scripts, there is no need
   * to explicitly add its JARs to Ant libraries.
   */
  @CompileDynamic
  static private def defineJunitTask(AntBuilder ant, String communityLib) {
    if (taskDefined) return
    taskDefined = true
    def junitTaskLoaderRef = "JUNIT_TASK_CLASS_LOADER"
    Path pathJUnit = new Path(ant.project)
    pathJUnit.createPathElement().setLocation(new File("$communityLib/ant/lib/ant-junit.jar"))
    pathJUnit.createPathElement().setLocation(new File("$communityLib/ant/lib/ant-junit4.jar"))
    ant.project.addReference(junitTaskLoaderRef, new AntClassLoader(ant.project.getClass().getClassLoader(), ant.project, pathJUnit))
    ant.taskdef(name: "junit", classname: "org.apache.tools.ant.taskdefs.optional.junit.JUnitTask", loaderRef: junitTaskLoaderRef)
  }

  private boolean isBootstrapSuiteDefault() {
    return options.bootstrapSuite == TestingOptions.BOOTSTRAP_SUITE_DEFAULT
  }
}