package com.zonbeozon.communityapp.exchangerate.fetch;

import com.zonbeozon.communityapp.exchangerate.fetch.dto.ExchangeRateFetchResult;

import java.time.LocalDate;

public interface ExchangeRateFetcher {
    ExchangeRateFetchResult fetch(LocalDate searchDate);
}
