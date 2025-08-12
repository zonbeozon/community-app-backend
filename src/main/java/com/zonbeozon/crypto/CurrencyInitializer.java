package com.zonbeozon.crypto;

import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.crypto.entity.LocalizedCurrencyInfo;
import com.zonbeozon.crypto.enums.LanguageCode;
import com.zonbeozon.crypto.loader.CurrencyRegistry;
import com.zonbeozon.crypto.loader.CurrencyRegistryHolder;
import com.zonbeozon.crypto.loader.MetadataLoader;
import com.zonbeozon.currency.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyInitializer {
    private final MetadataLoader metadataLoader;
    private final CurrencyRepository currencyRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initialize() {
        Set<String> existSymbols = currencyRepository.findAll().stream()
                .map(Currency::getSymbol)
                .collect(Collectors.toSet());

        CurrencyRegistryHolder holder = metadataLoader.load();
        //filtering duplicated currencies
        List<CurrencyRegistry> registries =  holder.getAll().stream()
                .filter(registry -> !existSymbols.contains(registry.getSymbol()))
                .toList();

        registries.forEach(registry -> {
            Currency currency = new Currency(registry.getSymbol(), registry.getLogo(), registry.getWebsite());
            currencyRepository.save(currency);

            Map<LanguageCode, LocalizedCurrencyInfo> currencyInfoMap = Arrays.stream(LanguageCode.values())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            code -> new LocalizedCurrencyInfo(currency, code)
                    ));
            registry.getNames().forEach((key, value) -> currencyInfoMap.get(key).setName(value));
            registry.getDescriptions().forEach((key, value) -> currencyInfoMap.get(key).setDescription(value));
            currency.getLocalizedInfo().addAll(currencyInfoMap.values().stream().toList());
        });

    }
}
