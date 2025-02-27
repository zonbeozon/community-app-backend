package com.zonbeozon.communityapp.crpyto.schedule;

import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyScheduler {
    private final CurrencyService currencyService;

    @Scheduled(fixedDelayString = "${scheduler.delay.currency}")
    public void run() {
        currencyService.updateCurrencyQuotes();
    }
}
