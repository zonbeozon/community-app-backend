package com.zonbeozon.market.repository;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MarketFiatMetricRepository extends JpaRepository<MarketFiatMetric, Long> {
    List<MarketFiatMetric> findByMarket(Market market);

    @Query("SELECT m FROM MarketFiatMetric m WHERE m.market.id = :marketId AND m.fiatType = :fiatType")
    Optional<MarketFiatMetric> findByMarketIdAndFiatType(Long marketId, FiatType fiatType);
}
