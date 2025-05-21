package com.zonbeozon.common.fetch;

public interface FetchResultConsumer<T> {
    void consumeFetchResult(T fetchResult);
}
