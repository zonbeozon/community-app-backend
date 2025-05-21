package com.zonbeozon.common;

import com.zonbeozon.common.fetch.FetchContextSupplier;
import com.zonbeozon.common.fetch.FetchManager;
import com.zonbeozon.common.fetch.FetchResultConsumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractFetchBasedUpdater<S, R> {
    private final FetchManager<S, R> fetchManager;

    protected void update(FetchContextSupplier<S> contextSupplier, FetchResultConsumer<R> resultConsumer) {
        resultConsumer.consumeFetchResult(fetchManager.fetch(contextSupplier));
    }

    protected R update(FetchContextSupplier<S> contextSupplier) {
        return fetchManager.fetch(contextSupplier);
    }
}
