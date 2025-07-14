package com.zonbeozon.global.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class BigDecimalUtils {
    public static final int PERCENT_SCALE = 3;
    public static final int PERCENT_PRECISION = 10;

    public static final int CRYPTO_SCALE = 8;
    public static final int CRYPTO_PRECISION = 23;

    public static final int FIAT_SCALE = 4;
    public static final int FIAT_PRECISION = 23;

    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal StringToBigDecimal(String value, int scale) {
        return new BigDecimal(value).setScale(scale, ROUNDING_MODE);
    }

    public static BigDecimal truc(BigDecimal value, int scale) {
        return value.setScale(scale, ROUNDING_MODE);
    }
}
