package com.zonbeozon.currency.test;

import java.math.BigDecimal;

public class CommonCurrencyRelatedData {
    // 공통 상수
    private static final BigDecimal USD_KRW_CONVERSION_RATE = BigDecimal.valueOf(1400);

    // BTC
    public static final String BTC_SYMBOL = "BTC";
    public static final String BTC_EN_NAME  = "bitcoin";
    public static final String BTC_KR_NAME = "비트코인";
    public static final Long BTC_RANK = 1L;
    public static final BigDecimal BTC_CIRCULATING_SUPPLY = BigDecimal.valueOf(19_000_000);
    public static final BigDecimal BTC_TOTAL_SUPPLY = BigDecimal.valueOf(21_000_000);
    public static final BigDecimal BTC_USD_PRICE = BigDecimal.valueOf(67_000);
    public static final BigDecimal BTC_KRW_PRICE = BTC_USD_PRICE.multiply(USD_KRW_CONVERSION_RATE);
    public static final BigDecimal BTC_USD_FULLY_DILUTED_MARKET_CAP = BTC_TOTAL_SUPPLY.multiply(BTC_USD_PRICE);
    public static final BigDecimal BTC_KRW_FULLY_DILUTED_MARKET_CAP = BTC_USD_FULLY_DILUTED_MARKET_CAP.multiply(USD_KRW_CONVERSION_RATE);
    public static final BigDecimal BTC_USD_MARKET_CAP = BigDecimal.valueOf(1_270_000_000_000L);
    public static final BigDecimal BTC_KRW_MARKET_CAP = BTC_USD_MARKET_CAP.multiply(USD_KRW_CONVERSION_RATE);
    public static final BigDecimal BTC_USD_VOLUME_24H = BigDecimal.valueOf(34_000_000_000L);
    public static final BigDecimal BTC_KRW_VOLUME_24H = BTC_USD_VOLUME_24H.multiply(USD_KRW_CONVERSION_RATE);
    public static final String BTC_KR_DESCRIPTION = "디지털 금이라 불리는 대표적인 암호화폐";
    public static final String BTC_EN_DESCRIPTION = "The leading cryptocurrency often referred to as digital gold.";
    public static final String BTC_LOGO = "bitcoin-logo.com";
    public static final String BTC_WEBSITE = "https://bitcoin.org";

    // ETH
    public static final String ETH_SYMBOL = "ETH";
    public static final String ETH_KR_NAME = "이더리움";
    public static final String ETH_EN_NAME = "ethereum";
    public static final Long ETH_RANK = 2L;
    public static final BigDecimal ETH_CIRCULATING_SUPPLY = BigDecimal.valueOf(120_000_000);
    public static final BigDecimal ETH_TOTAL_SUPPLY = BigDecimal.valueOf(120_500_000);
    public static final BigDecimal ETH_USD_PRICE = BigDecimal.valueOf(3_200);
    public static final BigDecimal ETH_KRW_PRICE = ETH_USD_PRICE.multiply(USD_KRW_CONVERSION_RATE);
    public static final BigDecimal ETH_USD_MARKET_CAP = BigDecimal.valueOf(384_000_000_000L);
    public static final BigDecimal ETH_KRW_MARKET_CAP = ETH_USD_MARKET_CAP.multiply(USD_KRW_CONVERSION_RATE);
    public static final BigDecimal ETH_USD_FULLY_DILUTED_MARKET_CAP = ETH_TOTAL_SUPPLY.multiply(ETH_USD_PRICE);
    public static final BigDecimal ETH_KRW_FULLY_DILUTED_MARKET_CAP = ETH_USD_FULLY_DILUTED_MARKET_CAP.multiply(USD_KRW_CONVERSION_RATE);
    public static final BigDecimal ETH_USD_VOLUME_24H = BigDecimal.valueOf(18_000_000_000L);
    public static final BigDecimal ETH_KRW_VOLUME_24H = ETH_USD_VOLUME_24H.multiply(USD_KRW_CONVERSION_RATE);
    public static final String ETH_KR_DESCRIPTION = "스마트 컨트랙트를 지원하는 플랫폼형 코인";
    public static final String ETH_EN_DESCRIPTION = "A platform-oriented cryptocurrency supporting smart contracts.";
    public static final String ETH_LOGO = "ethereum-logo.com";
    public static final String ETH_WEBSITE = "https://ethereum.org";
}
