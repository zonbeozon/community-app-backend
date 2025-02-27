package com.zonbeozon.communityapp.crpyto.domain.market;

import com.zonbeozon.communityapp.common.entity.BaseTimeEntity;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Market extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Long id;

    @Column(nullable = false)
    private String marketCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchange;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MarketType marketType;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MarketStatus marketStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticker_id")
    @Setter
    private Ticker ticker;

    @Builder
    public Market(
            String marketCode,
            Exchange exchange,
            MarketType marketType,
            Currency currency
    ) {
        this.marketCode = marketCode;
        this.exchange = exchange;
        this.marketType = marketType;
        this.marketStatus = MarketStatus.INACTIVE;
        this.currency = currency;
    }

    public void updateMarketStatus(MarketStatus status) {
        this.marketStatus = status;
    }
}
