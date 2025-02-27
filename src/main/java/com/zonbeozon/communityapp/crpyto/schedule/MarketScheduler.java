package com.zonbeozon.communityapp.crpyto.schedule;

import com.zonbeozon.communityapp.crpyto.service.market.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketScheduler {
    private final MarketService marketService;

    @Scheduled(fixedDelayString = "${scheduler.delay.market}")
    public void run() {
        marketService.updateMarkets();
    }
}
