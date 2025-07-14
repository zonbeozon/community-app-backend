package com.zonbeozon.fiat.fetch;

import com.zonbeozon.global.utils.BigDecimalUtils;
import com.zonbeozon.fiat.entity.ConversionRateCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class NaverKrwToUsdConversionRateFetcher implements ConversionRateFetcher {
    private final NaverUsdToKrwConversionRateFetcher naverUsdToKrwConversionRateFetcher;

    @Override
    public ConversionRateFetchResult fetch(ConversionRateCode code) {
        ConversionRateFetchResult result = naverUsdToKrwConversionRateFetcher.fetch(code);
        return new ConversionRateFetchResult(ConversionRateCode.KRW_USD, calReverseRate(result.value()));
    }

    @Override
    public boolean isSupportedExchangeRateCode(ConversionRateCode exchangeRateCode) {
        return exchangeRateCode == ConversionRateCode.KRW_USD;
    }

    private BigDecimal calReverseRate(BigDecimal value) {
        return BigDecimal.ONE.divide(value, BigDecimalUtils.FIAT_SCALE,  BigDecimalUtils.ROUNDING_MODE);
    }
}
