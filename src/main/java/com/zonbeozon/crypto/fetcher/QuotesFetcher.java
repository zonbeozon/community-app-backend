package com.zonbeozon.crypto.fetcher;

import com.zonbeozon.crypto.dto.CurrencyQuotesDto;

import java.util.Collection;
import java.util.Set;

public interface QuotesFetcher {
    Set<CurrencyQuotesDto> fetch(Collection<String> symbols);
}
