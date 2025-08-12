package com.zonbeozon.market.loader;

import com.zonbeozon.currency.service.CurrencyService;
import com.zonbeozon.market.service.MarketCodeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class MarketLoaderConfig {
    private final CurrencyService currencyService;
    private final MarketCodeResolver marketCodeResolver;

    @Bean
    public MarketLoader marketLoader() {
        JsonMarketLoader jsonMarketLoader = new JsonMarketLoader(marketCodeResolver, MarketRegistryHolder::new);
        return new CurrencyDecorator(jsonMarketLoader, currencyService);
    }
}
