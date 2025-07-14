package com.zonbeozon.market.entity;

import com.zonbeozon.global.utils.BigDecimalUtils;
import com.zonbeozon.fiat.entity.FiatType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@EqualsAndHashCode(of = "id", callSuper = false)
public class MarketFiatMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Market market;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FiatType fiatType;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal openingPrice;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal highPrice;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal lowPrice;

    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal tradePrice;
    /**
     * 24시간 기준 변동폭
     */
    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal signedChangePrice;

    /**
     * 24시간 기준 거래량
     */
    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal accTradePrice;

    public void update(MarketFiatMetric newFiatMetric) {
        this.openingPrice = newFiatMetric.getOpeningPrice();
        this.highPrice = newFiatMetric.getHighPrice();
        this.lowPrice = newFiatMetric.getLowPrice();
        this.tradePrice = newFiatMetric.getTradePrice();
        this.signedChangePrice = newFiatMetric.getSignedChangePrice();
        this.accTradePrice = newFiatMetric.getAccTradePrice();
    }

    public static MarketFiatMetric create(
            Market market,
            FiatType fiatType,
            BigDecimal openingPrice,
            BigDecimal highPrice,
            BigDecimal lowPrice,
            BigDecimal tradePrice,
            BigDecimal signedChangePrice,
            BigDecimal accTradePrice
    ) {
        MarketFiatMetric marketFiatMetrics = new MarketFiatMetric();
        marketFiatMetrics.market = market;
        marketFiatMetrics.fiatType = fiatType;
        marketFiatMetrics.openingPrice = openingPrice;
        marketFiatMetrics.highPrice = highPrice;
        marketFiatMetrics.lowPrice = lowPrice;
        marketFiatMetrics.tradePrice = tradePrice;
        marketFiatMetrics.signedChangePrice = signedChangePrice;
        marketFiatMetrics.accTradePrice = accTradePrice;
        return marketFiatMetrics;
    }

    public void truc() {
        this.openingPrice = BigDecimalUtils.truc(openingPrice, BigDecimalUtils.FIAT_SCALE);
        this.highPrice = BigDecimalUtils.truc(highPrice, BigDecimalUtils.FIAT_SCALE);
        this.lowPrice = BigDecimalUtils.truc(lowPrice, BigDecimalUtils.FIAT_SCALE);
        this.tradePrice = BigDecimalUtils.truc(tradePrice, BigDecimalUtils.FIAT_SCALE);
        this.signedChangePrice = BigDecimalUtils.truc(signedChangePrice, BigDecimalUtils.FIAT_SCALE);
        this.accTradePrice = BigDecimalUtils.truc(accTradePrice, BigDecimalUtils.FIAT_SCALE);
    }
}
