package com.zonbeozon.communityapp.exchangerate.domain;

import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.exception.FiatException;
import org.springframework.util.StringUtils;

public enum FiatType {
    KRW, USD;

    public static FiatType parse(String fiatType) {
        try {
            if(!StringUtils.hasText(fiatType)) throw new FiatException(ErrorCode.ILLEGAL_FIAT_TYPE);
            return FiatType.valueOf(fiatType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FiatException(ErrorCode.ILLEGAL_FIAT_TYPE);
        }
    }
}
