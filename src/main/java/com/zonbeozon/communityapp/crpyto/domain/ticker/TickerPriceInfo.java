package com.zonbeozon.communityapp.crpyto.domain.ticker;

import com.zonbeozon.communityapp.common.utils.BigDecimalUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class TickerPriceInfo {
    @Column(nullable = false, scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.SMALL_PRECISION)
    private BigDecimal openingPrice;

    @Column(nullable = false, scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.SMALL_PRECISION)
    private BigDecimal highPrice;

    @Column(nullable = false, scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.SMALL_PRECISION)
    private BigDecimal lowPrice;

    @Column(nullable = false, scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.SMALL_PRECISION)
    private BigDecimal tradePrice;

    @Column(nullable = false, scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.SMALL_PRECISION)
    private BigDecimal signedChangePrice;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.LARGE_PRECISION)
    private BigDecimal accTradePrice;
}
