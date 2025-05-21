package com.zonbeozon.fiat.fetch;

import com.zonbeozon.fiat.entity.ConversionRateCode;
import com.zonbeozon.fiat.exception.ConversionRateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConversionRateFetchManager {
    private final List<ConversionRateFetcher> fetchers;
    public ConversionRateFetchResult fetch(ConversionRateCode code) {
        ConversionRateFetcher conversionRateFetcher = fetchers.stream()
                .filter(fetcher -> fetcher.isSupportedExchangeRateCode(code))
                .findAny()
                .orElseThrow(() -> new ConversionRateException(code + "를 지원하는 fetcher가 등록되어 있지 않습니다."));
        return conversionRateFetcher.fetch(code);

    }

}
