package com.zonbeozon.global.annotation;


import jakarta.validation.constraints.NotNull;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public abstract class AnnotationUtils {

    /**
     * 메서드 시그니처만 사용가능
     * 메서드에 선언된 어노테이션, 메서드가 호출된 클래스의 어노테이션, 메서드가 호출된 클래스의 부모 클래스 어노테이션 까지 감지.
     * 모든 어노테이션의 한 단계 중첩 어노테이션까지 감지.
     */
    public static <T extends Annotation> Optional<T> getAnnotation(Class<T> clazz, ProceedingJoinPoint pjp) {
        var method = ((MethodSignature) pjp.getSignature()).getMethod();
        T annotation = method.getAnnotation(clazz);
        Optional<T> foundAnnotation = getAnnotation(clazz, method.getDeclaredAnnotations(), annotation);
        if (foundAnnotation.isPresent()) {
            return foundAnnotation;
        }
        for (Class<?> declaringClass = method.getDeclaringClass(); declaringClass != null; declaringClass = declaringClass.getDeclaringClass()) {
            annotation = declaringClass.getAnnotation(clazz);
            foundAnnotation = getAnnotation(clazz, method.getDeclaredAnnotations(), annotation);
            if (foundAnnotation.isPresent()) {
                return foundAnnotation;
            }
        }
        return Optional.empty();
    }

    private static <T extends Annotation> Optional<T> getAnnotation(Class<T> clazz, Annotation[] declaredAnnotations, T annotation) {
        if(annotation != null) {
            return Optional.of(annotation);
        }
        for (Annotation declaredAnnotation : declaredAnnotations) {
            annotation = declaredAnnotation.annotationType().getAnnotation(clazz);
            if(annotation != null) {
                return Optional.of(annotation);
            }
        }
        return Optional.empty();
    }

    public static <T extends Annotation, V> Optional<V> getValue(T annotation, String valueName, Class<V> valueType) {
        try {
            Method method = annotation.annotationType().getMethod(valueName);
            Object value = method.invoke(annotation);
            if(method.getReturnType().equals(valueType)) {
                return Optional.ofNullable(valueType.cast(value));
            }
            return Optional.empty();

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> getArgumentFromSignature(ProceedingJoinPoint pjp, String fieldName, Class<T> fieldType) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] parameterValues = pjp.getArgs();

        int indexOfField = Arrays.asList(parameterNames).indexOf(fieldName);
        if(indexOfField < 0 || parameterValues.length <= indexOfField) {
            return Optional.empty();
        }

        Object argument = parameterValues[indexOfField];
        if(fieldType.isInstance(argument)) {
            return Optional.of(fieldType.cast(argument));
        }
        return Optional.empty();
    }

}
