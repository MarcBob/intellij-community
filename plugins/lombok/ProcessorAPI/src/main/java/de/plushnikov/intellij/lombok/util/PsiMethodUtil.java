package de.plushnikov.intellij.lombok.util;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.light.LightMethod;
import de.plushnikov.intellij.lombok.psi.MyLightMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * @author Plushnikov Michail
 */
public class PsiMethodUtil {
  @NotNull
  public static PsiMethod createMethod(@NotNull PsiClass psiClass, @NotNull String methodText, @NotNull PsiElement navigationTarget) {
    PsiManager manager = psiClass.getContainingFile().getManager();
    PsiElementFactory elementFactory = JavaPsiFacade.getInstance(psiClass.getProject()).getElementFactory();

    PsiMethod method = elementFactory.createMethodFromText(methodText, psiClass);

    LightMethod lightMethod = new MyLightMethod(manager, method, psiClass);
    lightMethod.setNavigationElement(navigationTarget);
    return lightMethod;
  }

  public static boolean hasMethodByName(@NotNull PsiMethod[] classMethods, @NotNull String methodName) {
    boolean hasMethod = false;
    for (PsiMethod classMethod : classMethods) {
      if (classMethod.getName().equals(methodName)) {
        hasMethod = true;
        break;
      }
    }
    return hasMethod;
  }

  public static boolean hasMethodByName(@NotNull PsiMethod[] classMethods, String... methodNames) {
    boolean hasMethod = false;
    for (String methodName : methodNames) {
      if (hasMethodByName(classMethods, methodName)) {
        hasMethod = true;
        break;
      }
    }
    return hasMethod;
  }

  public static boolean hasMethodByName(@NotNull PsiMethod[] classMethods, @NotNull Collection<String> methodNames) {
    boolean hasMethod = false;
    for (String methodName : methodNames) {
      if (hasMethodByName(classMethods, methodName)) {
        hasMethod = true;
        break;
      }
    }
    return hasMethod;
  }
}
