package com.zonbeozon.market;

import com.zonbeozon.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
class MarketConfig {
    private final MarketService marketService;
    @Bean
    public MarketHolderSupplier marketHolderSupplier() {
        return () -> new HashmapMarketHolder(marketService.getAllMarkets());
    }
}
