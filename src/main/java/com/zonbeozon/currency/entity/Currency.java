package com.zonbeozon.currency.entity;

import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.global.utils.BigDecimalUtils;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.Market;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Currency extends BaseTimeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    @NotNull
    @Valid
    private CurrencyName name;

    @Embedded
    @NotNull
    @Valid
    private CurrencyDescription description;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String logo;

    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^[A-Z]+$", message = "심볼은 대문자 알파벳만 입력 가능합니다.")
    private String symbol;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String website;

    private Long currencyRank;

    @Column(scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.CRYPTO_PRECISION)
    private BigDecimal circulatingSupply;

    @Column(scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.CRYPTO_PRECISION)
    private BigDecimal totalSupply;

    @Valid
    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Market> markets = new HashSet<>();

    @Valid
    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CurrencyFiatMetric> currencyFiatMetrics = new HashSet<>();

    public void updateQuotes(Long currencyRank, BigDecimal circulatingSupply, BigDecimal totalSupply) {
        this.currencyRank = currencyRank;
        this.circulatingSupply = circulatingSupply;
        this.totalSupply = totalSupply;
    }

    public void updateEnDescription(String englishDescription) {
        this.description.updateEnDescription(englishDescription);
    }

    public void updateKrDescription(String krDescription) {
        this.description.updateKrDescription(krDescription);
    }

    public void updateFiatMetrics(Map<FiatType, CurrencyFiatMetric> fiatMetricMap) {
       currencyFiatMetrics.forEach(fiatMetric -> {
           CurrencyFiatMetric newFiatMetric = fiatMetricMap.get(fiatMetric.getFiatType());
           Assert.notNull(newFiatMetric, "fiatMetric is null");
           fiatMetric.update(newFiatMetric);
       });
    }

    public static Currency create(
            CurrencyName name,
            CurrencyDescription description,
            String logo,
            String symbol,
            String website
    ) {
        Currency currency = new Currency();
        currency.name = name;
        currency.description = description;
        currency.logo = logo;
        currency.symbol = symbol;
        currency.website = website;
        return currency;
    }
}
