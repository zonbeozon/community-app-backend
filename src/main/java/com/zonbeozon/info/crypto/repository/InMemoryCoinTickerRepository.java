package com.zonbeozon.info.crypto.repository;

import com.zonbeozon.info.crypto.domain.CoinTicker;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class InMemoryCoinTickerRepository implements CoinTickerRepository {
    private volatile Map<String, CoinTicker> store = Collections.emptyMap();

    public Optional<CoinTicker> findBySymbol(String symbol) {
        return Optional.ofNullable(store.get(symbol));
    }

    public List<CoinTicker> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Map<String, CoinTicker> findAllAsMap() {
        return new HashMap<>(store);
    }

    public void saveAll(Collection<CoinTicker> coinTickers) {
        synchronized (this) {
            Map<String, CoinTicker> newStore = new HashMap<>(this.store);

            for (CoinTicker info : coinTickers) {
                newStore.put(info.getSymbol(), info);
            }

            this.store = newStore;
        }
    }

    public void replaceAll(Collection<CoinTicker> coinTickers) {
        Map<String, CoinTicker> newStore = new HashMap<>(coinTickers.stream().collect(Collectors.toMap(
                CoinTicker::getSymbol,
                Function.identity()
        )));
        synchronized (this) {
            this.store = newStore;
        }
    }
}
