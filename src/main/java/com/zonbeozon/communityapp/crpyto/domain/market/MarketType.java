package com.zonbeozon.communityapp.crpyto.domain.market;

public enum MarketType {
    KRW,BTC,USDT;

    public static MarketType fromPairString(String pair) {
        if (pair == null || !pair.contains("-")) {
            throw new IllegalArgumentException("Invalid market request string: " + pair);
        }

        // Extract the last part after the '-' character
        String marketTypeString = pair.substring(pair.lastIndexOf('-') + 1);

        // Match with existing MarketType
        try {
            return MarketType.valueOf(marketTypeString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid MarketType: " + marketTypeString, e);
        }
    }
}
