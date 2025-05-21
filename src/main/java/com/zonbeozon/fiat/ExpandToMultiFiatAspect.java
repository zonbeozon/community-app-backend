package com.zonbeozon.fiat;

import com.zonbeozon.fiat.service.FiatConverterResolver;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ExpandToMultiFiatAspect {
    private final FiatConverterResolver fiatConverterResolver;

    @AfterReturning(
            pointcut = "@annotation(com.zonbeozon.fiat.ExpandToMultiFiat)",
            returning = "result"
    )
    public void afterMarketUpdate(Object result) {
        if (result != null) {
            fiatConverterResolver.resolve(result);
        }
    }
}
