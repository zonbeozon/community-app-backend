package com.zonbeozon.market.entity;

public enum MarketType {
    KRW, USDT;

    public static MarketType parse(String marketType) {
        return MarketType.valueOf(marketType.toUpperCase());
    }
}
