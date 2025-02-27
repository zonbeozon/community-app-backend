package com.zonbeozon.communityapp.crpyto.domain.market;

import com.zonbeozon.communityapp.crpyto.exception.MarketException;
import com.zonbeozon.communityapp.exception.ErrorCode;

public enum MarketStatus {
    INACTIVE, ACTIVE;

    public static MarketStatus parse(String status) {
        try {
            return MarketStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MarketException(ErrorCode.ILLEGAL_MARKET_STATUS);
        }
    }
}
