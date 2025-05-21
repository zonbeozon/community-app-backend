package com.zonbeozon.market.repository;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketFiatMetricRepository extends JpaRepository<MarketFiatMetric, Long> {
    List<MarketFiatMetric> findByMarket(Market market);
    List<MarketFiatMetric> findByMarketAndFiatType(Market market, FiatType fiatType);
}
