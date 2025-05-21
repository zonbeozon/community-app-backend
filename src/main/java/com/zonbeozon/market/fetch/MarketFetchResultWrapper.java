package com.zonbeozon.market.fetch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class MarketFetchResultWrapper {
    private final List<MarketFetchResult> fetchResults;

    public void applyAll(Consumer<MarketFetchResult> consumer) {
        fetchResults.forEach(consumer);
    }
}
