package com.zonbeozon.market.repository;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MarketRepository extends JpaRepository<Market, Long> {
    Optional<Market> findByMarketCodeAndExchange(String marketCode, Exchange exchange);
    List<Market> findByCurrencyId(Long currencyId);
}
