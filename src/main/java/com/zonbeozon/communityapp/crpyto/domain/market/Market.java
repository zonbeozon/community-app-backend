package com.zonbeozon.communityapp.crpyto.domain.market;

import com.zonbeozon.communityapp.common.entity.BaseTimeEntity;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

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
    @Column(nullable = false)
    private String koreanName;
    @Column(nullable = false)
    private String englishName;

    @OneToMany(mappedBy = "market")
    private Set<ExchangeMarket> exchangeMarkets = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticker_id")
    @Setter
    private Ticker ticker;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MarketType marketType;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private MarketStatus marketStatus;

    @Builder
    private Market(
            String marketCode,
            String koreanName,
            String englishName,
            MarketType marketType) {
        this.marketCode = marketCode;
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.marketType = marketType;
        this.marketStatus = MarketStatus.ACTIVE;
    }

    public static Market fromDto(MarketRequest marketRequest) {
        return Market.builder()
                .marketCode(marketRequest.marketCode())
                .englishName(marketRequest.englishName())
                .koreanName(marketRequest.koreanName())
                .marketType(marketRequest.marketType())
                .build();
    }
}
