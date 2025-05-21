package com.zonbeozon.currency.entity;

import com.zonbeozon.common.utils.BigDecimalUtils;
import com.zonbeozon.fiat.entity.FiatType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class CurrencyFiatMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FiatType fiatType;

    @Column(scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    @NotNull
    private BigDecimal marketCap;

    @Column(scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    @NotNull
    private BigDecimal fullyDilutedMarketCap;

    @Column(scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    @NotNull
    private BigDecimal volume;

    public static CurrencyFiatMetric create(
            FiatType fiatType,
            Currency currency,
            BigDecimal marketCap,
            BigDecimal fullyDilutedMarketCap,
            BigDecimal volume
    ) {
        CurrencyFiatMetric currencyFiatMetrics = new CurrencyFiatMetric();
        currencyFiatMetrics.currency = currency;
        currencyFiatMetrics.fiatType = fiatType;
        currencyFiatMetrics.marketCap = marketCap;
        currencyFiatMetrics.fullyDilutedMarketCap = fullyDilutedMarketCap;
        currencyFiatMetrics.volume = volume;
        return currencyFiatMetrics;
    }

    public void update(CurrencyFiatMetric newFiatMetric) {
        this.marketCap = newFiatMetric.marketCap;
        this.fullyDilutedMarketCap = newFiatMetric.fullyDilutedMarketCap;
        this.volume = newFiatMetric.volume;
    }
}
