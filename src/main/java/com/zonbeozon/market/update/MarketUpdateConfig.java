package com.zonbeozon.market.update;

import com.zonbeozon.fiat.entity.FiatType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@RequiredArgsConstructor
public class MarketUpdateConfig {
    private final MarketUpdateHandler marketUpdateHandler;
    private final MultiFiatConvertProxy multiFiatConvertProxy;

    @Bean
    @Primary
    public MarketFetchDataHandler marketFetchDataHandler() {
        return marketFetchData -> {
            marketUpdateHandler.handle(marketFetchData);
            multiFiatConvertProxy.handle(marketFetchData, FiatType.getAll());
        };
    }



}
