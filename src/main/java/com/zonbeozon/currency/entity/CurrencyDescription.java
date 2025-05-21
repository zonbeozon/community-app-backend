package com.zonbeozon.currency.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrencyDescription {
    public static final int MAX_CONTENT_LENGTH = 10000;
    public static final int MIN_CONTENT_LENGTH = 1;

    @Column(columnDefinition = "TEXT")
    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
    private String enDescription;
    @Column(columnDefinition = "TEXT")
    @Size(min = MIN_CONTENT_LENGTH, max = MAX_CONTENT_LENGTH)
    @NotNull
    private String krDescription;

    public void updateEnDescription(String enDescription) {
        this.enDescription = enDescription;
    }

    public void updateKrDescription(String krDescription) {
        this.krDescription = krDescription;
    }
}
