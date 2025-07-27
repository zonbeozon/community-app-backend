package com.zonbeozon;

import com.zonbeozon.currency.loader.CurrencyLoadManager;
import com.zonbeozon.currency.updater.CurrencyUpdater;
import com.zonbeozon.fiat.service.ConversionRateService;
import com.zonbeozon.market.loader.MarketLoadManager;
import com.zonbeozon.market.update.MarketUpdater;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
class GlobalScheduler {
    private final ConversionRateService conversionRateService;
    private final MarketUpdater marketUpdater;
    private final CurrencyUpdater currencyUpdater;
    private final MarketLoadManager marketLoadManager;
    private final CurrencyLoadManager currencyLoadManager;

    @PostConstruct
    public void init() {
        conversionRateService.updateConversionRate();
        log.debug("initial Conversion Rate added");

//        currencyLoadManager.loadThenAdd();
//        log.debug("initial Currency added");
//
//        marketLoadManager.loadThenAdd();
//        log.debug("initial Market added");
    }

    @Scheduled(cron = "${scheduler.cron.conversion-rate}")
    public void updateConversionRate() {
        conversionRateService.updateConversionRate();
        log.debug("Conversion Rate updated");
    }

//    @Scheduled(fixedDelayString = "${scheduler.delay.market}")
//    public void updateMarket() {
//        marketUpdater.update();
//        log.debug("Market updated");
//    }
//
//    @Scheduled(fixedDelayString = "${scheduler.delay.currency}")
//    public void updateCurrency() {
//        currencyUpdater.update();
//        log.debug("Currency updated");
//    }
}
