package com.zonbeozon.crypto.entity;

import com.zonbeozon.crypto.enums.LanguageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LocalizedCurrencyInfo {
    @EmbeddedId
    private LocalizedCurrencyInfoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("currencyId")
    @JoinColumn(name = "currency_id", insertable = false, updatable = false, nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false, nullable = false)
    private LanguageCode languageCode;

    @Setter
    private String name;

    @Column(columnDefinition = "TEXT")
    @Setter
    private String description;

    public LocalizedCurrencyInfo(Currency currency, LanguageCode languageCode) {
        this.id = new LocalizedCurrencyInfoId(currency.getId(), languageCode);
        this.currency = currency;
        this.languageCode = languageCode;
    }
}
