package com.zonbeozon.global;

import com.zonbeozon.global.fetch.FetchContextSupplier;
import com.zonbeozon.global.fetch.FetchManager;
import com.zonbeozon.global.fetch.FetchResultConsumer;
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
