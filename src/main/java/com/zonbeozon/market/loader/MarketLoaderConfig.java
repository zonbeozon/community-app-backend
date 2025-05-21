package com.zonbeozon.market.loader;

import com.zonbeozon.currency.service.CurrencyService;
import com.zonbeozon.market.service.MarketCodeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class MarketLoaderConfig {
    private static final String FILE_PATH = "/data/markets.json";
    private final CurrencyService currencyService;
    private final MarketCodeResolver marketCodeResolver;

    @Bean
    public MarketLoader marketLoader() {
        JsonMarketLoader jsonMarketLoader = new JsonMarketLoader(marketCodeResolver, FILE_PATH, MarketRegistryHolder::new);
        return new CurrencyDecorator(jsonMarketLoader, currencyService);
    }
}
