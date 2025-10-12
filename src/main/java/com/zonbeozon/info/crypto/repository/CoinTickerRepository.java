package com.zonbeozon.info.crypto.repository;

import com.zonbeozon.info.crypto.domain.CoinTicker;

import java.util.*;

public interface CoinTickerRepository {
    Optional<CoinTicker> findBySymbol(String symbol);
    List<CoinTicker> findAll();
    Map<String, CoinTicker> findAllAsMap();

    void saveAll(Collection<CoinTicker> coinTickers);

    default void replaceAll(Collection<CoinTicker> coinTickers) {
        throw new UnsupportedOperationException();
    }
}
