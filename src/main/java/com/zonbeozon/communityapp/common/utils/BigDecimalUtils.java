package com.zonbeozon.communityapp.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 크립토 서비스 로직에서 허용하는 소수점 자리수는 1 사토시(10의 -8승) 따라서 SCALE을 8로 설정
 * 개별 코인 가격 중 가장 높은 값인 원화 기준 비트코인을 나타내기 위해서는 정수 부분이 9자리 필요함 따라서 PRECISION은 8 + 9 = 17로 설정
 * 코인의 마캣캡이나 거래량은 일반적으로 이보다 훨씬 높기때문에 정수 부분을 15로 정함 따라서 LARGE_PRECISION은 8 + 15 = 23으로 설정
 */
public final class BigDecimalUtils {
    public static final int CRYPTO_SCALE = 8;
    public static final int FIAT_SCALE = 2;
    public static final int LARGE_PRECISION = 23;
    public static final int SMALL_PRECISION = 17;

    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal createBigDecimal(String number, int scale) {
        return new BigDecimal(number).setScale(scale, ROUNDING_MODE);
    }
    public static BigDecimal createBigDecimal(Double number, int scale) {
        return new BigDecimal(number).setScale(scale, ROUNDING_MODE);
    }
}
