package org.jetbrains.jps.incremental;

import org.jetbrains.jps.incremental.groovy.GroovyBuilder;
import org.jetbrains.jps.incremental.java.JavaBuilder;
import org.jetbrains.jps.incremental.resourses.ResourcesBuilder;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eugene Zhuravlev
 *         Date: 9/17/11
 */
public class BuilderRegistry {
  private static class Holder {
    static final BuilderRegistry ourInstance = new BuilderRegistry();
  }
  private final Map<BuilderCategory, List<Builder>> myBuilders = new HashMap<BuilderCategory, List<Builder>>();
  private ExecutorService myTasksExecutor;

  public static BuilderRegistry getInstance() {
    return Holder.ourInstance;
  }

  private BuilderRegistry() {
    for (BuilderCategory category : BuilderCategory.values()) {
      myBuilders.put(category, new ArrayList<Builder>());
    }
    final Runtime runtime = Runtime.getRuntime();
    myTasksExecutor = Executors.newFixedThreadPool(runtime.availableProcessors());
    runtime.addShutdownHook(new Thread() {
      public void run() {
        myTasksExecutor.shutdownNow();
      }
    });

    // todo: some builder registration mechanism for plugins needed

    myBuilders.get(BuilderCategory.TRANSLATOR).add(new GroovyBuilder(true));
    myBuilders.get(BuilderCategory.TRANSLATOR).add(new JavaBuilder(myTasksExecutor));
    myBuilders.get(BuilderCategory.TRANSLATOR).add(new ResourcesBuilder());
    myBuilders.get(BuilderCategory.TRANSLATOR).add(new GroovyBuilder(false));

  }

  public List<BuildTask> getBeforeTasks(){
    return Collections.emptyList(); // todo
  }

  public List<BuildTask> getAfterTasks(){
    return Collections.emptyList(); // todo
  }

  public List<Builder> getBuilders(BuilderCategory category){
    return myBuilders.get(category); // todo
  }

  public void shutdown() {
    myTasksExecutor.shutdownNow();
  }

}
