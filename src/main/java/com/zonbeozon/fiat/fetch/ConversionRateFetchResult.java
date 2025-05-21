package com.zonbeozon.fiat.fetch;

import com.zonbeozon.fiat.entity.ConversionRate;
import com.zonbeozon.fiat.entity.ConversionRateCode;

import java.math.BigDecimal;

public record ConversionRateFetchResult (
     ConversionRateCode conversionRateCode,
     BigDecimal value
) {
    public ConversionRate toEntity() {
        return new ConversionRate(conversionRateCode, value);
    }
}
