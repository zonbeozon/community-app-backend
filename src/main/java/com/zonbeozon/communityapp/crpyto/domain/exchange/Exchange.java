package com.zonbeozon.communityapp.crpyto.domain.exchange;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_id")
    private Long id;

    @Column(nullable = false)
    private String englishName;
    @Column(nullable = false)
    private String koreanName;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketType topPriorityMarketType;

    @OneToMany(mappedBy = "exchange")
    private List<Market> markets = new LinkedList<>();

    @Builder
    public Exchange(String englishName, String koreanName, String description, MarketType topPriorityMarketType) {
        this.englishName = englishName;
        this.koreanName = koreanName;
        this.description = description;
        this.topPriorityMarketType = topPriorityMarketType;
    }
}
