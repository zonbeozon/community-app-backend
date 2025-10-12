package com.zonbeozon.info.crypto.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class CoinInfoDataHealthIndicator {
    private final AtomicReference<Instant> atomicLastTickerSuccessTime = new AtomicReference<>();
    private final AtomicReference<Instant> atomicLastMetadataSuccessTime = new AtomicReference<>();

    @Value("${health-check.data.stale-threshold-minutes}")
    private long staleThresholdMinutes;

    public void recordTickerSuccess() {
        Instant now = Instant.now();
        this.atomicLastTickerSuccessTime.set(now);
        log.debug("Ticker 데이터 갱신 성공 기록. 마지막 성공 시간: {}", atomicLastTickerSuccessTime);
    }

    public void recordMetadataSuccess() {
        Instant now = Instant.now();
        this.atomicLastMetadataSuccessTime.set(now);
        log.debug("Metadata 데이터 갱신 성공 기록. 마지막 성공 시간: {}", atomicLastMetadataSuccessTime);
    }

    public void checkDataHealth() {
        Instant lastTickerSuccessTime = atomicLastTickerSuccessTime.get();
        Instant lastMetadataSuccessTime = atomicLastMetadataSuccessTime.get();

        if (lastTickerSuccessTime == null || lastMetadataSuccessTime == null) {
            log.error("데이터가 완전히 초기화되지 않음. Ticker 성공: {}, Metadata 성공: {}",
                    lastTickerSuccessTime != null, lastMetadataSuccessTime != null);
            throw new ServiceUnavailableException(ErrorCode.SERVICE_NOT_READY);
        }

        Instant now = Instant.now();
        long staleThresholdSeconds = staleThresholdMinutes * 60;
        if (Duration.between(lastTickerSuccessTime, now).getSeconds() > staleThresholdSeconds) {
            log.error("Ticker 데이터가 {}분 이상 갱신되지 않음. 장애 상태로 간주", staleThresholdMinutes);
            throw new ServiceUnavailableException(ErrorCode.DATA_IS_STALE);
        }

        if (Duration.between(lastMetadataSuccessTime, now).getSeconds() > staleThresholdSeconds) {
            log.error("Metadata 데이터가 {}분 이상 갱신되지 않음. 장애 상태로 간주.", staleThresholdMinutes);
            throw new ServiceUnavailableException(ErrorCode.DATA_IS_STALE);
        }
    }
}
