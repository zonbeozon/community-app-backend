package com.zonbeozon.communityapp.crpyto.domain.exchange;

import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "exchange")
    private Set<ExchangeMarket> exchangeMarkets = new HashSet<>();

    private Exchange(String englishName, String koreanName, String description) {
        this.englishName = englishName;
        this.koreanName = koreanName;
        this.description = description;
    }

    public static Exchange fromDto(ExchangeRequest exchangeRequest) {
        return new Exchange(
                exchangeRequest.englishName(),
                exchangeRequest.koreanName(),
                exchangeRequest.description());
    }
}
