package com.zonbeozon.currency.service.dto;

import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.crypto.entity.CurrencyQuote;
import com.zonbeozon.fiat.entity.FiatType;

import java.math.BigDecimal;

public record CurrencyResponse (
        Long id,
        String krName,
        String enName,
        String symbol,
        String enDescription,
        String logo,
        Long rank,
        BigDecimal circulatingSupply,
        BigDecimal totalSupply,
        CurrencyFiatMetricResponse fiatMetric
) {
    public static CurrencyResponse from(Currency currency, FiatType fiatType) {
        CurrencyQuote currencyQuote = currency.getCurrencyQuotes().stream()
                .filter(metric -> metric.getFiatType() == fiatType)
                .findAny()
                .orElseThrow(() -> new IllegalStateException(fiatType + "에 해당하는 객체를 찾을 수 없습니다."));
        return new CurrencyResponse(
                currency.getId(),
                currency.getName().getKrName(),
                currency.getName().getEnName(),
                currency.getSymbol(),
                currency.getDescription().getEnDescription(),
                currency.getLogo(),
                currency.getCurrencyRank(),
                currency.getCirculatingSupply(),
                currency.getTotalSupply(),
                CurrencyFiatMetricResponse.from(currencyQuote)
        );
    }
}
