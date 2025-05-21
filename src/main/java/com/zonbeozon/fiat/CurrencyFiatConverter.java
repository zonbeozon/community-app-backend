package com.zonbeozon.fiat;

import com.zonbeozon.currency.CurrencyHolder;
import com.zonbeozon.currency.entity.CurrencyFiatMetric;
import com.zonbeozon.currency.service.CurrencyFiatMetricService;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.fiat.service.ConversionRateService;
import com.zonbeozon.market.MarketHolder;
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
class CurrencyFiatConverter implements FiatConverter {
    private final ConversionRateService conversionRateService;
    private final CurrencyFiatMetricService currencyFiatMetricService;

    @Override
    @Transactional
    public void convert(Object source) {
        CurrencyHolder currencyHolder = (CurrencyHolder) source;
        currencyHolder.applyAll(
                currency -> {
                    Map<FiatType, CurrencyFiatMetric> existCurrencyFiatMetricMap = currencyFiatMetricService.getCurrencyFiatMetricsByCurrency(currency).stream()
                            .collect(Collectors.toMap(CurrencyFiatMetric::getFiatType, Function.identity()));
                    CurrencyFiatMetric existCurrencyFiatMetric = existCurrencyFiatMetricMap.values().stream().findAny()
                            .orElseThrow(() -> new IllegalStateException("통화 변환을 위해서는 최소 한개의 CurrencyFiatMetric이 존재해야 한다."));
                    for (FiatType fiatType : getAllFiatTypes()) {
                        if(existCurrencyFiatMetricMap.containsKey(fiatType))
                            continue;
                        BigDecimal conversionRate = conversionRateService.getConversionRate(existCurrencyFiatMetric.getFiatType(), fiatType);
                        CurrencyFiatMetric convertedFiatMetric = multiplyFiatValues(existCurrencyFiatMetric, fiatType, conversionRate);
                        currencyFiatMetricService.addCurrencyFiatMetric(convertedFiatMetric);
                    }
                }
        );

    }

    private List<FiatType> getAllFiatTypes() {
        return FiatType.getAll();
    }

    private CurrencyFiatMetric multiplyFiatValues(CurrencyFiatMetric from, FiatType to, BigDecimal conversionRate) {
        return CurrencyFiatMetric.create(
                to,
                from.getCurrency(),
                from.getMarketCap().multiply(conversionRate),
                from.getFullyDilutedMarketCap().multiply(conversionRate),
                from.getVolume().multiply(conversionRate)
        );
    }

    @Override
    public boolean isSupported(Object source) {
        return source instanceof CurrencyHolder;
    }
}
