package com.zonbeozon.communityapp.exchangerate.domain;

import com.zonbeozon.communityapp.common.utils.BigDecimalUtils;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_rate_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false, scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.SMALL_PRECISION)
    private BigDecimal rate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExchangeRateCode code;

    @Builder
    public ExchangeRate(LocalDateTime updatedAt, BigDecimal rate, ExchangeRateCode code) {
        this.updatedAt = updatedAt;
        this.rate = rate;
        this.code = code;
    }

    public void update(LocalDateTime updatedAt, BigDecimal rate) {
        this.updatedAt = updatedAt;
        this.rate = rate;
    }
}
