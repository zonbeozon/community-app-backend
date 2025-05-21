package com.zonbeozon.fiat;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.fiat.service.ConversionRateService;
import com.zonbeozon.market.MarketHolder;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.service.MarketFiatMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class MarketFiatConverter implements FiatConverter {
    private final ConversionRateService conversionRateService;
    private final MarketFiatMetricService fiatMetricService;
    private final MarketFiatMetricService marketFiatMetricService;

    @Override
    @Transactional
    public void convert(Object source) {
        MarketHolder marketHolder = (MarketHolder) source;
        marketHolder.applyAll(
                market -> {
                    Map<FiatType, MarketFiatMetric> existMarketFiatMetricMap = fiatMetricService.getMarketFiatMetricsByMarket(market)
                            .stream().collect(Collectors.toMap(MarketFiatMetric::getFiatType, Function.identity()));
                    MarketFiatMetric existMarketFiatMetric = existMarketFiatMetricMap.values().stream().findAny()
                            .orElseThrow(() -> new IllegalStateException("통화 변환을 위해서는 최소 한개의 MarketFiatMetric이 존재해야 한다."));
                    for (FiatType fiatType : getAllFiatTypes()) {
                        if(existMarketFiatMetricMap.containsKey(fiatType))
                            continue;
                        BigDecimal conversionRate = conversionRateService.getConversionRate(existMarketFiatMetric.getFiatType(), fiatType);
                        MarketFiatMetric convertedFiatMetric = multiplyFiatValues(existMarketFiatMetric, fiatType, conversionRate);
                        marketFiatMetricService.addMarketFiatMetric(convertedFiatMetric);
                    }
                }
        );

    }

    private List<FiatType> getAllFiatTypes() {
        return FiatType.getAll();
    }

    private MarketFiatMetric multiplyFiatValues(MarketFiatMetric from, FiatType to, BigDecimal conversionRate) {
        return MarketFiatMetric.create(
                from.getMarket(),
                to,
                from.getOpeningPrice().multiply(conversionRate),
                from.getHighPrice().multiply(conversionRate),
                from.getLowPrice().multiply(conversionRate),
                from.getTradePrice().multiply(conversionRate),
                from.getSignedChangePrice().multiply(conversionRate),
                from.getAccTradePrice().multiply(conversionRate)
        );
    }

    @Override
    public boolean isSupported(Object source) {
        return source instanceof MarketHolder;
    }
}
