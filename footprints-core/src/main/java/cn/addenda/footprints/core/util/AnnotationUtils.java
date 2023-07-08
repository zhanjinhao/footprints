package cn.addenda.footprints.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author addenda
 * @since 2022/8/7 22:35
 */
public class AnnotationUtils {

    private AnnotationUtils() {
    }

    public static <T extends Annotation> T extractAnnotationFromMethod(Class<?> aClass, String methodName, Class<T> annotationClass) {
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Annotation[] methodAnnotations = method.getAnnotations();
                for (Annotation annotation : methodAnnotations) {
                    if (annotationClass.isAssignableFrom(annotation.getClass())) {
                        return (T) annotation;
                    }
                }
            }
        }
        return null;
    }

    public static <T extends Annotation> T extractAnnotationFromClass(Class<?> aClass, Class<T> annotationClass) {
        Annotation[] annotations = aClass.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotationClass.isAssignableFrom(annotation.getClass())) {
                return (T) annotation;
            }
        }
        return null;
    }

}
