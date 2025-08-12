package com.zonbeozon.crypto.fetcher;

public record CurrencyMetaData(
        String symbol,
        String name,
        String description,
        String logo,
        String website
) {
}
