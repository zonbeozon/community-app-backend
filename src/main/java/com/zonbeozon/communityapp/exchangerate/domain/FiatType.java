package com.zonbeozon.communityapp.exchangerate.domain;

import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.exception.FiatException;

public enum FiatType {
    KRW, USD;

    public static FiatType parse(String fiatType) {
        try {
            return FiatType.valueOf(fiatType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FiatException(ErrorCode.FIAT_TYPE_NOT_FOUND);
        }
    }
}
