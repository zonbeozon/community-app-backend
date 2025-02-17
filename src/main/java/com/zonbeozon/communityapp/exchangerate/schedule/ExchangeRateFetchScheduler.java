package com.zonbeozon.communityapp.exchangerate.schedule;

import com.zonbeozon.communityapp.exchangerate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class ExchangeRateFetchScheduler {
    private final ExchangeRateService exchangeRateService;

    @Scheduled(cron = "${scheduler.exchange-rate}")
    public void updateExchangeRate() {
        exchangeRateService.updateExchangeRate();
    }
}
