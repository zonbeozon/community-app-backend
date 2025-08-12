package com.zonbeozon.crypto.fetcher;

import com.zonbeozon.crypto.enums.LanguageCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class MetadataFetchResult {
    //symbol map
    private final LanguageCode languageCode;
    private final Set<CurrencyMetaData> content;
}
