package com.zonbeozon.currency.test;

import com.zonbeozon.fiat.entity.FiatType;

import static com.zonbeozon.currency.test.CommonCurrencyRelatedData.*;

public class CurrencyFiatMetricDummy {
    public static final CurrencyFiatMetricDto BTC_USD_METRIC_DTO = new CurrencyFiatMetricDto(
            FiatType.USD,
            BTC_USD_MARKET_CAP,
            BTC_USD_FULLY_DILUTED_MARKET_CAP,
            BTC_USD_VOLUME_24H
    );
    public static final CurrencyFiatMetricDto BTC_KRW_METRIC_DTO = new CurrencyFiatMetricDto(
            FiatType.KRW,
            BTC_KRW_MARKET_CAP,
            BTC_KRW_FULLY_DILUTED_MARKET_CAP,
            BTC_KRW_VOLUME_24H
    );

    public static final CurrencyFiatMetricDto ETH_USD_METRIC_DTO = new CurrencyFiatMetricDto(
            FiatType.USD,
            ETH_USD_MARKET_CAP,
            ETH_USD_FULLY_DILUTED_MARKET_CAP,
            ETH_USD_VOLUME_24H
    );
    public static final CurrencyFiatMetricDto ETH_KRW_METRIC_DTO = new CurrencyFiatMetricDto(
            FiatType.KRW,
            ETH_KRW_MARKET_CAP,
            ETH_KRW_FULLY_DILUTED_MARKET_CAP,
            ETH_KRW_VOLUME_24H
    );
}
