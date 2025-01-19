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
@Setter
public class Exchange {
    @Id
    @GeneratedValue
    @Column(name = "exchange_id")
    private Long id;

    private String englishName;
    private String koreanName;
    private String description;

    @OneToMany(mappedBy = "exchange")
    private Set<ExchangeMarket> exchangeMarkets = new HashSet<>();

    public static Exchange fromDto(ExchangeRequest exchangeRequest) {
        Exchange exchange = new Exchange();
        exchange.setEnglishName(exchangeRequest.getEnglishName());
        exchange.setKoreanName(exchangeRequest.getKoreanName());
        exchange.setDescription(exchangeRequest.getDescription());
        return exchange;
    }
}
