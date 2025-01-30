package com.zonbeozon.communityapp.crpyto.domain.ticker;

import com.zonbeozon.communityapp.common.entity.BaseTimeEntity;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticker extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticker_id")
    private Long id;

    @Column(nullable = false)
    private String marketCode;
    @Column(nullable = false)
    private Double openingPrice;
    @Column(nullable = false)
    private Double highPrice;
    @Column(nullable = false)
    private Double lowPrice;
    @Column(nullable = false)
    private Double tradePrice;
    @Column(nullable = false)
    private Double signedChangePrice;
    @Column(nullable = false)
    private Double signedChangeRate;
    @Column(nullable = false)
    private Double accTradePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchange;

    @OneToOne(mappedBy = "ticker")
    private Market market;

    @Builder
    private Ticker(
            String marketCode,
            Double openingPrice,
            Double highPrice,
            Double lowPrice,
            Double tradePrice,
            Double signedChangePrice,
            Double signedChangeRate,
            Double accTradePrice
    ) {
        this.marketCode = marketCode;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradePrice = tradePrice;
        this.signedChangePrice = signedChangePrice;
        this.signedChangeRate = signedChangeRate;
        this.accTradePrice = accTradePrice;
    }

    public static Ticker fromDto(TickerRequest tickerRequest) {
        return Ticker.builder()
                .marketCode(tickerRequest.marketCode())
                .openingPrice(tickerRequest.openingPrice())
                .highPrice(tickerRequest.highPrice())
                .lowPrice(tickerRequest.lowPrice())
                .tradePrice(tickerRequest.tradePrice())
                .signedChangePrice(tickerRequest.signedChangePrice())
                .signedChangeRate(tickerRequest.signedChangeRate())
                .accTradePrice(tickerRequest.accTradePrice())
                .build();
    }
}
