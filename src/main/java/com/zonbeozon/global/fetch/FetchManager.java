package com.zonbeozon.global.fetch;

public interface FetchManager<S, R> {
    R fetch(FetchContextSupplier<S> contextSupplier);
}
