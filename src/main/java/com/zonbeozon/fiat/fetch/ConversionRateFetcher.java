package com.zonbeozon.fiat.fetch;

import com.zonbeozon.fiat.entity.ConversionRateCode;

public interface ConversionRateFetcher {
    ConversionRateFetchResult fetch(ConversionRateCode code);
    boolean isSupportedExchangeRateCode(ConversionRateCode exchangeRateCode);
}
