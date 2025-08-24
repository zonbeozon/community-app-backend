package com.zonbeozon.global.cache;

import com.zonbeozon.global.annotation.AnnotationUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public abstract class AbstractCacheEvictAspect<KEY> {
    private final Class<? extends Annotation> annotationClass;
    private final Class<KEY> keyType;

    @Pointcut
    protected abstract void callAt();

    @Around("callAt()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Annotation annotation = AnnotationUtils.getAnnotation(annotationClass, joinPoint).orElseThrow();
        String keyName = AnnotationUtils.getValue(annotation, "keyParameterName", String.class).orElseThrow();
        KEY keyValue = AnnotationUtils.getArgumentFromSignature(joinPoint, keyName, keyType).orElseThrow();
        evict(keyValue);
        return joinPoint.proceed();
    }

    protected abstract void evict(KEY key);

}
