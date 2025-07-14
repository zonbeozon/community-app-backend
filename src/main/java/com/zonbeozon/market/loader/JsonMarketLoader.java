package com.zonbeozon.market.loader;

import com.zonbeozon.global.ListFileLoaderTemplate;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.MarketType;
import com.zonbeozon.market.service.MarketCodeResolver;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
class JsonMarketLoader implements MarketLoader {
    private final static String DELIMITER = ":";

    private final ListFileLoaderTemplate<MarketJsonMappingDto> jsonFileLoaderTemplate = new ListFileLoaderTemplate<>(MarketJsonMappingDto.class);
    private final MarketCodeResolver marketCodeResolver;
    private final String filePath;
    private final Supplier<MarketRegistryHolder> registryHolderSupplier;

    @Override
    public MarketRegistryHolder load() {
        MarketRegistryHolder registryHolder = registryHolderSupplier.get();
        jsonFileLoaderTemplate.load(filePath).stream()
                .map(this::createMarkets)
                .flatMap(List::stream)
                .forEach(registryHolder::add);

        return registryHolder;
    }

    private List<MarketRegistry> createMarkets(MarketJsonMappingDto mapping) {
        return mapping.getMarkets().stream().map(
                exchangeMarketPair -> {
                    String[] parts = exchangeMarketPair.split(DELIMITER);
                    Exchange exchange = Exchange.parse(parts[0]);
                    MarketType marketType = MarketType.parse(parts[1]);
                    MarketRegistry marketRegistry = new MarketRegistry();
                    marketRegistry.setSymbol(mapping.getSymbol());
                    marketRegistry.setMarketCode(marketCodeResolver.resolve(exchange, mapping.getSymbol(), marketType));
                    marketRegistry.setExchange(exchange);
                    marketRegistry.setMarketType(marketType);
                    return marketRegistry;
                }).toList();
    }
}
