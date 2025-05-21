package com.zonbeozon.market.repository;

import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {
    List<Market> findByMarketCodeIn(List<String> marketCodes);
    List<Market> findByCurrencyId(Long currencyId);
}
