package com.zonbeozon.currency.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CurrencyName {
    @NotNull
    @Pattern(regexp = "^[a-z]+$", message = "영문명은 소문자 알파벳만 입력 가능합니다.")
    private String enName;
    @NotNull
    @Pattern(regexp = "^[가-힣]+$", message = "한글명은 한글만 입력 가능합니다.")
    private String krName;
}
