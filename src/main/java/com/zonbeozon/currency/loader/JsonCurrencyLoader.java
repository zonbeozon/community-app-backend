package com.zonbeozon.currency.loader;

import com.zonbeozon.crypto.loader.CurrencyRegistry;
import com.zonbeozon.crypto.loader.CurrencyRegistryHolder;
import com.zonbeozon.global.ListFileLoaderTemplate;

import java.util.function.Supplier;

class JsonCurrencyLoader implements CurrencyLoader {
    private final String filePath;
    private final ListFileLoaderTemplate<CurrencyJsonMappingDto> jsonFileLoaderTemplate;
    private final Supplier<CurrencyRegistryHolder> currencyRegistryHolderSupplier;

    public JsonCurrencyLoader(String filePath, Supplier<CurrencyRegistryHolder> currencyRegistryHolderSupplier) {
        this.filePath = filePath;
        this.jsonFileLoaderTemplate = new ListFileLoaderTemplate<>(CurrencyJsonMappingDto.class);
        this.currencyRegistryHolderSupplier = currencyRegistryHolderSupplier;
    }

    @Override
    public CurrencyRegistryHolder load() {
        CurrencyRegistryHolder registryHolder = currencyRegistryHolderSupplier.get();
        jsonFileLoaderTemplate.load(filePath).forEach(mappingDto -> registryHolder.add(createRegistry(mappingDto)));
        return registryHolder;
    }

    private CurrencyRegistry createRegistry(CurrencyJsonMappingDto jsonMappingDto) {
        CurrencyRegistry currencyRegistry =  new CurrencyRegistry();
        currencyRegistry.setKrName(jsonMappingDto.getKrName());
        currencyRegistry.setKrDescription(jsonMappingDto.getKrDescription());
        currencyRegistry.setSymbol(jsonMappingDto.getSymbol());
        return currencyRegistry;
    }
}
