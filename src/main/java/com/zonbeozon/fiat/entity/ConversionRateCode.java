package com.zonbeozon.fiat.entity;

import com.zonbeozon.fiat.exception.ConversionRateException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ConversionRateCode {
    USD_KRW(FiatType.USD, FiatType.KRW),
    KRW_USD(FiatType.KRW, FiatType.USD);

    private final FiatType from;
    private final FiatType to;
    public static ConversionRateCode parse(FiatType from, FiatType to) {
        return Arrays.stream(ConversionRateCode.values())
                .filter(code -> code.from == from && code.to == to)
                .findFirst()
                .orElseThrow(() -> new ConversionRateException("지원하지 않는 환율 코드입니다: " + from + " -> " + to));
    }
}

