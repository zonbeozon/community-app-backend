package com.zonbeozon.global.Indicator;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class DataHealthIndicator {
    // volatile이나 AtomicBoolean으로 동시성 문제 방지
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private volatile Instant lastSuccessTime;

    // 데이터 로딩 성공 시 호출
    public void recordSuccess() {
        isInitialized.set(true);
        lastSuccessTime = Instant.now();
    }

    // API에서 데이터 상태를 확인할 때 사용
    public boolean isDataReady() {
        // 최초 로딩이 한 번이라도 성공했는지 확인
        return isInitialized.get();
    }

    public Instant getLastSuccessTime() {
        return lastSuccessTime;
    }
}