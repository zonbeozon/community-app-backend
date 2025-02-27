package com.zonbeozon.communityapp.crpyto.domain.currency;

import com.zonbeozon.communityapp.common.entity.BaseTimeEntity;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Currency extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "currency_id")
    private Long id;

    @Column(nullable = false)
    private String englishName;
    @Column(nullable = false)
    private String koreanName;
    @Lob
    @Column(nullable = false, length = 100000)
    private String englishDescription;
    @Column(nullable = false)
    private String koreanDescription;
    @Column(nullable = false)
    private String logo;
    @Column(nullable = false)
    private String symbol;
    @Column(nullable = false)
    private String website;
    @Column(nullable = false)
    private Long currencyRank;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "circulatingSupply", column = @Column(name = "circulating_supply_krw")),
            @AttributeOverride(name = "totalSupply", column = @Column(name = "total_supply_krw")),
            @AttributeOverride(name = "marketCap", column = @Column(name = "market_cap_krw")),
            @AttributeOverride(name = "fullyDilutedMarketCap", column = @Column(name = "fully_diluted_market_cap_krw")),
            @AttributeOverride(name = "volume", column = @Column(name = "volume_krw"))
    })
    private CurrencyStatsInfo currencyStatsInfoKrw;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "circulatingSupply", column = @Column(name = "circulating_supply_usd")),
            @AttributeOverride(name = "totalSupply", column = @Column(name = "total_supply_usd")),
            @AttributeOverride(name = "marketCap", column = @Column(name = "market_cap_usd")),
            @AttributeOverride(name = "fullyDilutedMarketCap", column = @Column(name = "fully_diluted_market_cap_usd")),
            @AttributeOverride(name = "volume", column = @Column(name = "volume_usd"))
    })
    private CurrencyStatsInfo currencyStatsInfoUsd;

    @OneToMany(mappedBy = "currency")
    private List<Market> markets = new ArrayList<>();

    @Builder
    public Currency(
            String englishName,
            String koreanName,
            String englishDescription,
            String koreanDescription,
            String logo,
            String symbol,
            String website,
            Long currencyRank,
            CurrencyStatsInfo currencyStatsInfoKrw,
            CurrencyStatsInfo currencyStatsInfoUsd
    ) {
        this.englishName = englishName;
        this.koreanName = koreanName;
        this.englishDescription = englishDescription;
        this.koreanDescription = koreanDescription;
        this.logo = logo;
        this.symbol = symbol;
        this.website = website;
        this.currencyRank = currencyRank;
        this.currencyStatsInfoKrw = currencyStatsInfoKrw;
        this.currencyStatsInfoUsd = currencyStatsInfoUsd;
    }

    public void updateQuote(long rank, CurrencyStatsInfo currencyStatsInfoKrw, CurrencyStatsInfo currencyStatsInfoUsd) {
        this.currencyRank = rank;
        this.currencyStatsInfoKrw = currencyStatsInfoKrw;
        this.currencyStatsInfoUsd = currencyStatsInfoUsd;
    }

    public void updateEnglishDescription(String englishDescription) {
        this.englishDescription = englishDescription;
    }

    public void updateKoreanDescription(String koreanDescription) {
        this.koreanDescription = koreanDescription;
    }
}
