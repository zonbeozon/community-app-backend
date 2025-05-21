package com.zonbeozon.currency.loader;

import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyLoadManager {
    private final CurrencyLoader currencyLoader;
    private final CurrencyService currencyService;

    public void loadThenAdd() {
        CurrencyRegistryHolder registryHolder = currencyLoader.load();
        List<Currency> currencies =  registryHolder.toEntity();
        currencyService.addCurrencies(currencies);
    }
}
