package com.zonbeozon.fiat.fetch;

import java.util.List;

public record NaverConversionRateResponse(
        Long pkid,
        int count,
        List<CountryInfo> country,
        String calculatorMessage
) {
    public record CountryInfo(
            String value,
            String subValue,
            String currencyUnit
    ) {}
}
