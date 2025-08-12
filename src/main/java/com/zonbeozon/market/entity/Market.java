package com.zonbeozon.market.entity;

import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.global.utils.BigDecimalUtils;
import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.exchange.Exchange;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Market extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String marketCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Exchange exchange;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private MarketType marketType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @NotNull
    private Currency currency;

    @Column(scale = BigDecimalUtils.PERCENT_SCALE, precision = BigDecimalUtils.PERCENT_PRECISION)
    private BigDecimal signedChangeRate;

    private Market(
            String marketCode,
            Exchange exchange,
            MarketType marketType,
            Currency currency
    ) {
        this.marketCode = marketCode;
        this.exchange = exchange;
        this.marketType = marketType;
        this.currency = currency;
    }

    public void updateSignedChangeRate(BigDecimal signedChangeRate) {
        this.signedChangeRate = signedChangeRate;
    }

    public static Market create(
            String marketCode,
            Exchange exchange,
            MarketType marketType,
            Currency currency
    ) {
        return new Market(marketCode, exchange, marketType, currency);
    }
}
