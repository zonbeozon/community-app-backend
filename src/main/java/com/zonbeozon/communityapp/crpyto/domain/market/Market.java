package com.zonbeozon.communityapp.crpyto.domain.market;

import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Market {
    @Id
    @GeneratedValue
    @Column(name = "market_id")
    private Long id;

    private String marketCode;
    private String koreanName;
    private String englishName;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "market")
    private Set<ExchangeMarket> exchangeMarkets = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticker_id")
    private Ticker ticker;

    private Market(
            String marketCode,
            String koreanName,
            String englishName) {
        this.marketCode = marketCode;
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    public static Market fromDto(MarketRequest marketRequest) {
        return new Market(
                marketRequest.getMarketCode(),
                marketRequest.getKoreanName(),
                marketRequest.getEnglishName()
        );
    }
}
