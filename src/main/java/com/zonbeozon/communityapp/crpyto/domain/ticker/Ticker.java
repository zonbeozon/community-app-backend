package com.zonbeozon.communityapp.crpyto.domain.ticker;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerRequest;
import com.zonbeozon.communityapp.crpyto.fetch.upbit.dto.UpbitTickerRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticker {
    @Id
    @GeneratedValue
    @Column(name = "ticker_id")
    private Long id;

    private String marketCode;
    private Double openingPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double tradePrice;
    private Double signedChangePrice;
    private Double signedChangeRate;
    private Double accTradePrice;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;

    @OneToOne(mappedBy = "ticker")
    private Market market;

    private Ticker(
            String marketCode,
            Double openingPrice,
            Double highPrice,
            Double lowPrice,
            Double tradePrice,
            Double signedChangePrice,
            Double signedChangeRate,
            Double accTradePrice,
            LocalDateTime updatedAt) {
        this.marketCode = marketCode;
        this.openingPrice = openingPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.tradePrice = tradePrice;

        this.signedChangePrice = signedChangePrice;
        this.signedChangeRate = signedChangeRate;
        this.accTradePrice = accTradePrice;
        this.updatedAt = updatedAt;
    }

    public static Ticker fromDto(TickerRequest tickerRequest) {
        return new Ticker(
                tickerRequest.getMarketCode(),
                tickerRequest.getOpeningPrice(),
                tickerRequest.getHighPrice(),
                tickerRequest.getLowPrice(),
                tickerRequest.getTradePrice(),
                tickerRequest.getSignedChangePrice(),
                tickerRequest.getSignedChangeRate(),
                tickerRequest.getAccTradePrice(),
                tickerRequest.getUpdatedAt()
        );

    }
}
