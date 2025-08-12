package com.zonbeozon.crypto.entity;

import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.global.utils.BigDecimalUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Currency extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String symbol;

    @Column(columnDefinition = "TEXT")
    private String logo;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocalizedCurrencyInfo> localizedInfo = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String website;

    private Long currencyRank;

    @Column(scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.CRYPTO_PRECISION)
    private BigDecimal circulatingSupply;

    @Column(scale = BigDecimalUtils.CRYPTO_SCALE, precision = BigDecimalUtils.CRYPTO_PRECISION)
    private BigDecimal totalSupply;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurrencyQuote> currencyQuotes = new ArrayList<>();

    public Currency(
            String symbol,
            String logo,
            String website
    ) {
        this.symbol = symbol;
        this.logo = logo;
        this.website = website;
    }
}
