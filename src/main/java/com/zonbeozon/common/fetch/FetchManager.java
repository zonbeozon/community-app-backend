package com.zonbeozon.common.fetch;

public interface FetchManager<S, R> {
    R fetch(FetchContextSupplier<S> contextSupplier);
}
