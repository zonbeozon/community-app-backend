package com.zonbeozon.global.fetch;

public interface FetchResultConsumer<T> {
    void consumeFetchResult(T fetchResult);
}
