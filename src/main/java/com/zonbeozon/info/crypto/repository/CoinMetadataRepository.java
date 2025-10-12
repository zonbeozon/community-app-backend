package com.zonbeozon.info.crypto.repository;

import com.zonbeozon.info.crypto.domain.CoinMetadata;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CoinMetadataRepository {
    Optional<CoinMetadata> findBySymbol(String symbol);

    List<CoinMetadata> findAll();
    void saveAll(Collection<CoinMetadata> coinMetadata);

    default void replaceAll(Collection<CoinMetadata> coinMetadata) {
        throw new UnsupportedOperationException();
    }
}
