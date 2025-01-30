package com.zonbeozon.communityapp.crpyto.domain.market;

public enum MarketType {
    KRW,BTC,USDT;

    public static MarketType fromPairString(String pair, boolean reversed) {
        if (pair == null || !pair.contains("-")) {
            throw new IllegalArgumentException("Invalid market request string: " + pair);
        }

        String marketTypeString = reversed
                ? pair.substring(pair.indexOf('-') + 1) // ex) btc-krw에서 krw추출
                : pair.substring(0, pair.indexOf('-')); // ex) krw-btc에서 krw추출

        try {
            return MarketType.valueOf(marketTypeString.toUpperCase()); // 대소문자 구분 없이 처리
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid MarketType: " + marketTypeString, e);
        }
    }
}
