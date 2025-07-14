package com.zonbeozon.global;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class AspectUtils {
    public static <T> T extractParameter(JoinPoint joinPoint, String paramName, Class<T> requiredType) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        T extractedValue = null;

        for (int i = 0; i < parameterNames.length; i++) {
            String currentParamName = parameterNames[i];
            Object arg = args[i];

            if (currentParamName.equals(paramName)) {
                if (requiredType.isInstance(arg)) {
                    extractedValue = requiredType.cast(arg);
                    break;
                } else {
                    throw new IllegalArgumentException(
                            "파라미터 '" + paramName + "'의 타입은 " + requiredType.getSimpleName() + "이어야 합니다. 현재 타입: " + (arg != null ? arg.getClass().getSimpleName() : "null")
                    );
                }
            }
        }

        if (extractedValue == null) {
            throw new IllegalArgumentException("필수 파라미터 '" + paramName + "'가 메서드 인자에 존재하지 않습니다.");
        }
        return extractedValue;
    }
}
