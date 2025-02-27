package com.zonbeozon.communityapp.crpyto.domain.market.repository;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketStatus;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MarketRepository extends JpaRepository<Market, Long> {
    @Query("""
        SELECT m FROM Market m
        JOIN FETCH m.ticker
        JOIN FETCH m.exchange
        WHERE m.currency.id = :currencyId
        AND m.marketStatus = 'ACTIVE'
    """)
    List<Market> findActiveMarketsWithTickerAndExchangeByCurrencyId(Long currencyId);

    @Query("""
        SELECT m FROM Market m
        JOIN FETCH m.ticker
        JOIN FETCH m.currency
        WHERE m.exchange.id = :exchangeId
        AND m.marketStatus = 'ACTIVE'
        AND m.marketType = :marketType
    """)
    List<Market> findActiveMarketsWithTickerAndCurrencyByExchangeAndMarketType(Long exchangeId, MarketType marketType);

    @Query("""
        SELECT m FROM Market m
        LEFT JOIN FETCH m.ticker
        WHERE m.exchange = :exchange
        AND m.marketStatus = 'ACTIVE'
    """)
    List<Market> findActiveMarketsWithTickerByExchange(Exchange exchange);

}
