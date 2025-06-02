package com.zonbeozon.market.service;

import com.zonbeozon.common.EntityValidator;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.exception.MarketFiatMetricNotFoundException;
import com.zonbeozon.market.repository.MarketFiatMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MarketFiatMetricService {
    private final MarketFiatMetricRepository marketFiatMetricRepository;
    private final EntityValidator validator;

    @Transactional(readOnly = true)
    public List<MarketFiatMetric> getMarketFiatMetricsByMarket(Market market) {
        return marketFiatMetricRepository.findByMarket(market);
    }

    @Transactional(readOnly = true)
    public Optional<MarketFiatMetric> getMarketFiatMetricByMarketIdAndFiatType(Long marketId , FiatType fiatType) {
        return marketFiatMetricRepository.findByMarketIdAndFiatType(marketId, fiatType);
    }

    @Transactional
    public void addMarketFiatMetric(MarketFiatMetric marketFiatMetric) {
        validator.validate(marketFiatMetric);
        marketFiatMetric.truc();
        marketFiatMetricRepository.save(marketFiatMetric);
    }


}
