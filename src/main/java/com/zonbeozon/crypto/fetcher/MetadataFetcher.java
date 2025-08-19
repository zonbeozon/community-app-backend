package com.zonbeozon.crypto.fetcher;

import com.zonbeozon.crypto.dto.CurrencyMetadataDto;

import java.util.Collection;
import java.util.Set;

public interface MetadataFetcher {
    Set<CurrencyMetadataDto> fetch(Collection<String> symbols);
}
