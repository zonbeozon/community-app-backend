package com.zonbeozon.crypto.entity;

import com.zonbeozon.global.utils.BigDecimalUtils;
import com.zonbeozon.fiat.entity.FiatType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrencyQuote {
    @EmbeddedId
    private CurrencyQuoteId id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("currencyId")
    @JoinColumn(name = "currency_id", insertable = false, updatable = false, nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, insertable = false)
    private FiatType fiatType;

    @Column(scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal marketCap;

    @Column(scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal fullyDilutedMarketCap;

    @Column(scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal volume;


    public CurrencyQuote(
            Currency currency,
            FiatType fiatType,
            BigDecimal marketCap,
            BigDecimal fullyDilutedMarketCap,
            BigDecimal volume
    ) {
        this.id = new CurrencyQuoteId(currency.getId(), fiatType);
        this.currency = currency;
        this.fiatType = fiatType;
        this.marketCap = marketCap;
        this.fullyDilutedMarketCap = fullyDilutedMarketCap;
        this.volume = volume;
    }
}
