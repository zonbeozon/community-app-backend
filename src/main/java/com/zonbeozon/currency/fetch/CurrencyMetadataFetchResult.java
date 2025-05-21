package com.zonbeozon.currency.fetch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class CurrencyMetadataFetchResult {
    private final List<CurrencyFetchMetaData> metadata;
}
