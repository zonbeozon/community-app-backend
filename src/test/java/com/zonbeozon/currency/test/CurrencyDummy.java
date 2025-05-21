package com.zonbeozon.currency.test;

import com.zonbeozon.currency.loader.CurrencyJsonMappingDto;

import java.util.List;

import static com.zonbeozon.currency.test.CommonCurrencyRelatedData.*;
import static com.zonbeozon.currency.test.CurrencyFiatMetricDummy.*;

public class CurrencyDummy {
    public static final CurrencyJsonMappingDto BTC_DTO = new CurrencyJsonMappingDto(
            BTC_SYMBOL,
            BTC_KR_NAME,
            BTC_EN_NAME,
            BTC_KR_DESCRIPTION,
            BTC_EN_DESCRIPTION,
            BTC_LOGO,
            BTC_WEBSITE,
            BTC_RANK,
            BTC_CIRCULATING_SUPPLY,
            BTC_TOTAL_SUPPLY,
            List.of(BTC_KRW_METRIC_DTO, BTC_USD_METRIC_DTO)
    );

    public static final CurrencyJsonMappingDto ETH_DTO = new CurrencyJsonMappingDto(
            ETH_SYMBOL,
            ETH_KR_NAME,
            ETH_EN_NAME,
            ETH_KR_DESCRIPTION,
            ETH_EN_DESCRIPTION,
            ETH_LOGO,
            ETH_WEBSITE,
            ETH_RANK,
            ETH_CIRCULATING_SUPPLY,
            ETH_TOTAL_SUPPLY,
            List.of(ETH_KRW_METRIC_DTO, ETH_USD_METRIC_DTO)
    );
}
