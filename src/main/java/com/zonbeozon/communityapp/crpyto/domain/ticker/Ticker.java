package com.zonbeozon.communityapp.crpyto.domain.ticker;

import com.zonbeozon.communityapp.common.entity.BaseTimeEntity;
import com.zonbeozon.communityapp.common.utils.BigDecimalUtils;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticker extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticker_id")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "openingPrice", column = @Column(name = "opening_price_krw")),
            @AttributeOverride(name = "highPrice", column = @Column(name = "high_price_krw")),
            @AttributeOverride(name = "lowPrice", column = @Column(name = "low_price_krw")),
            @AttributeOverride(name = "tradePrice", column = @Column(name = "trade_price_krw")),
            @AttributeOverride(name = "signedChangePrice", column = @Column(name = "signed_change_price_krw")),
            @AttributeOverride(name = "accTradePrice", column = @Column(name = "acc_trade_price_krw"))
    })
    private TickerPriceInfo tickerPriceInfoKrw;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "openingPrice", column = @Column(name = "opening_price_usd")),
            @AttributeOverride(name = "highPrice", column = @Column(name = "high_price_usd")),
            @AttributeOverride(name = "lowPrice", column = @Column(name = "low_price_usd")),
            @AttributeOverride(name = "tradePrice", column = @Column(name = "trade_price_usd")),
            @AttributeOverride(name = "signedChangePrice", column = @Column(name = "signed_change_price_usd")),
            @AttributeOverride(name = "accTradePrice", column = @Column(name = "acc_trade_price_usd"))
    })
    private TickerPriceInfo tickerPriceInfoUsd;

    @Column(nullable = false, scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.SMALL_PRECISION)
    private BigDecimal signedChangeRate;

    @OneToOne(mappedBy = "ticker", cascade = CascadeType.REMOVE , orphanRemoval = true)
    private Market market;

    @Builder
    public Ticker(
            TickerPriceInfo tickerPriceInfoUsd,
            TickerPriceInfo tickerPriceInfoKrw,
            BigDecimal signedChangeRate
    ) {
        this.tickerPriceInfoUsd = tickerPriceInfoUsd;
        this.tickerPriceInfoKrw = tickerPriceInfoKrw;
        this.signedChangeRate = signedChangeRate;
    }

    public void update(Ticker ticker) {
        this.tickerPriceInfoUsd = ticker.tickerPriceInfoUsd;
        this.tickerPriceInfoKrw = ticker.tickerPriceInfoKrw;
        this.signedChangeRate = ticker.signedChangeRate;
    }
}
