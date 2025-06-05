package com.zonbeozon.market.loader;

import com.zonbeozon.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketLoadManager {
    private final MarketLoader marketLoader;
    private final MarketService marketService;

    public void loadThenAdd() {
        MarketRegistryHolder registryHolder = marketLoader.load();
        marketService.addMarkets(registryHolder.toEntity());
    }
}
