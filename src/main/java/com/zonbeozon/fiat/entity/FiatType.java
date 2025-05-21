package com.zonbeozon.fiat.entity;

import com.zonbeozon.fiat.exception.FiatException;
import com.zonbeozon.fiat.exception.FiatNotFoundException;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public enum FiatType {
    KRW, USD;

    public static FiatType parse(String fiatType) {
        try {
            if(!StringUtils.hasText(fiatType)) throw new FiatNotFoundException(fiatType + "은 존재하지 않는 fiatType입니다.");
            return FiatType.valueOf(fiatType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FiatNotFoundException(fiatType + "은 존재하지 않는 fiatType입니다.");
        }
    }

    public static List<FiatType> getAll() {
        return Arrays.asList(FiatType.values());
    }
}
