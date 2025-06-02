package com.zonbeozon.market.update;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.fiat.service.ConversionRateService;
import com.zonbeozon.market.fetch.MarketFetchData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
class MultiFiatConvertProxy {
    private final ConversionRateService conversionRateService;
    private final MarketFiatMetricUpdateHandler fiatMetricUpdateHandler;

    public void handle(MarketFetchData marketFetchData, List<FiatType> fiatTypes) {
        fiatTypes.forEach(fiatType -> {
            MarketFetchData convertedData = convertMarketFetchData(marketFetchData, fiatType);
            fiatMetricUpdateHandler.handle(convertedData);
        });
    }

    private MarketFetchData convertMarketFetchData(MarketFetchData from, FiatType to) {
        BigDecimal rate = conversionRateService.getConversionRate(from.fiatType(), to);
        return new MarketFetchData(
                from.marketId(),
                to,
                from.marketCode(),
                from.openingPrice().multiply(rate),
                from.highPrice().multiply(rate),
                from.lowPrice().multiply(rate),
                from.tradePrice().multiply(rate),
                from.signedChangePrice().multiply(rate),
                from.signedChangeRate(),
                from.accTradePrice().multiply(rate)
        );
    }
}
