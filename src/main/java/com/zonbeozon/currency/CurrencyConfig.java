package com.zonbeozon.currency;

import com.zonbeozon.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CurrencyConfig {
    private final CurrencyService currencyService;


    @Bean
    public CurrencyHolderSupplier currencyHolderSupplier() {
        return () -> new HashMapCurrencyRegistryHolder(currencyService.getAllCurrencies());
    }
}
