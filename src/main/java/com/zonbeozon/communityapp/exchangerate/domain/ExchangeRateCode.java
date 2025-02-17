package com.zonbeozon.communityapp.exchangerate.domain;

public enum ExchangeRateCode {
    USD;

    public static boolean isSupported(String code) {
        try {
            ExchangeRateCode.valueOf(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
