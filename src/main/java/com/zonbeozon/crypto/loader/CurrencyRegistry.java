package com.zonbeozon.crypto.loader;

import com.zonbeozon.crypto.enums.LanguageCode;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@Builder
public class CurrencyRegistry {
    private String symbol;
    private Map<LanguageCode, String> names;
    private Map<LanguageCode, String> descriptions;
    private String logo;
    private String website;
    private Long currencyRank;
}
