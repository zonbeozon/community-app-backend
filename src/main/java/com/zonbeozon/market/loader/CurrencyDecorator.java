package com.zonbeozon.market.loader;

import com.zonbeozon.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CurrencyDecorator implements MarketLoader {
    private final MarketLoader prevMarketLoader;
    private final CurrencyService currencyService;

    @Override
    public MarketRegistryHolder load() {
        MarketRegistryHolder registryHolder = prevMarketLoader.load();
        registryHolder.applyAll(
                registry -> registry.setCurrency(currencyService.getBySymbolOrThrow(registry.getSymbol()))
        );
        return registryHolder;
    }
}
