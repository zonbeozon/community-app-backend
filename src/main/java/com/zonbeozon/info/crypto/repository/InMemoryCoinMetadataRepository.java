package com.zonbeozon.info.crypto.repository;

import com.zonbeozon.info.crypto.domain.CoinMetadata;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class InMemoryCoinMetadataRepository implements CoinMetadataRepository {
    private volatile Map<String, CoinMetadata> store = Collections.emptyMap();

    @Override
    public Optional<CoinMetadata> findBySymbol(String symbol) {
        return Optional.ofNullable(store.get(symbol));
    }

    @Override
    public List<CoinMetadata> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void saveAll(Collection<CoinMetadata> coinMetadata) {
        synchronized (this) {
            Map<String, CoinMetadata> newStore = new HashMap<>(this.store);

            for (CoinMetadata metadata : coinMetadata) {
                newStore.put(metadata.getSymbol(), metadata);
            }
            this.store = newStore;
        }
    }

    @Override
    public void replaceAll(Collection<CoinMetadata> coinMetadata) {
        Map<String, CoinMetadata> newStore = new HashMap<>(coinMetadata.stream().collect(Collectors.toMap(
                CoinMetadata::getSymbol,
                Function.identity()
        )));
        synchronized (this) {
            this.store = newStore;
        }
    }
}
