package com.zonbeozon.communityapp.crpyto.domain.currency;

import com.zonbeozon.communityapp.common.utils.BigDecimalUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class CurrencyStatsInfo {
    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.LARGE_PRECISION)
    private BigDecimal circulatingSupply;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.LARGE_PRECISION)
    private BigDecimal totalSupply;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.LARGE_PRECISION)
    private BigDecimal marketCap;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.LARGE_PRECISION)
    private BigDecimal fullyDilutedMarketCap;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.LARGE_PRECISION)
    private BigDecimal volume;
}
