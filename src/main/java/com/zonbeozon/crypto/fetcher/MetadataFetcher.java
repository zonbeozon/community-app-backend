package com.zonbeozon.crypto.fetcher;

import java.util.Collection;

public interface MetadataFetcher {
    MetadataFetchResult fetch(Collection<String> symbols);
}
