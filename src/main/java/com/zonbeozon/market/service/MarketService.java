package com.zonbeozon.market.service;

import com.zonbeozon.common.EntityValidator;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.exception.MarketNotFoundException;
import com.zonbeozon.market.repository.MarketRepository;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.service.dto.MarketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MarketService {
    private final MarketRepository marketRepository;
    private final EntityValidator validator;
    private final MarketFiatMetricService marketFiatMetricService;

    public void addMarkets(List<Market> markets) {
        validator.validate(markets);
        List<Market> filteredMarkets = filterDuplicateMarketsByMarketCode(markets);
        marketRepository.saveAll(filteredMarkets);
    }

    private List<Market> filterDuplicateMarketsByMarketCode(List<Market> markets) {
        Set<String> duplicateMarketCodes = marketRepository.findByMarketCodeIn(getMarketCodes(markets)).stream()
                .map(Market::getMarketCode).collect(Collectors.toCollection(HashSet::new));
        return markets.stream()
                .filter(market -> !duplicateMarketCodes.contains(market.getMarketCode()))
                .collect(Collectors.toList());
    }

    private List<String> getMarketCodes(List<Market> markets) {
        return markets.stream().map(Market::getMarketCode).toList();
    }

    public void deleteMarket(Long marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketNotFoundException(marketId + "는 존재하지 않는 marketId 입니다."));
        marketRepository.delete(market);
    }

    @Transactional(readOnly = true)
    public MarketResponse getMarketResponse(Long marketId, FiatType fiatType) {
        Market market = getMarketByIdOrThrow(marketId);
        List<MarketFiatMetric> marketFiatMetrics = marketFiatMetricService.getMarketFiatMetricsByMarketAndFiatType(market, fiatType);
        return MarketResponse.from(market, marketFiatMetrics);
    }

    private Market getMarketByIdOrThrow(Long marketId) {
        return marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketNotFoundException(marketId + "는 존재하지 않는 id입니다."));
    }

    @Transactional(readOnly = true)
    public List<Market> getAllMarkets() {
        return marketRepository.findAll();
    }
}
