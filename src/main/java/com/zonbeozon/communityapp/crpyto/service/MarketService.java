package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketStatus;
import com.zonbeozon.communityapp.crpyto.domain.market.repository.MarketRepository;
import com.zonbeozon.communityapp.crpyto.fetch.dto.MarketFetchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MarketService {
    private final MarketRepository marketRepository;
    private final ExchangeMarketService exchangeMarketService;
    private final ExchangeService exchangeService;

    public void updateMarkets(MarketFetchResult marketFetchResult) {
        Exchange exchange = exchangeService.findByName(marketFetchResult.getExchangeName());
        List<Market> existMarkets = exchangeMarketService.getMarketsByExchange(exchange);

        //prepare for listing or initial update
        List<Market> marketsNotInDb = filterMarketsNotInDb(marketFetchResult, existMarkets);
        if(!marketsNotInDb.isEmpty()) {
            marketRepository.saveAll(marketsNotInDb);
            marketsNotInDb.forEach(market -> exchangeMarketService.add(market, exchange));
        }
        //prepare for delisting
        List<Market> marketsInDbButNotInFetchResult = filterMarketsInDbButNotInFetchResult(marketFetchResult, existMarkets);
        if(!marketsInDbButNotInFetchResult.isEmpty()) {
            marketsInDbButNotInFetchResult.forEach(market -> updateStatus(market, MarketStatus.DELISTED));
        }
    }

    public void updateStatus(Market market, MarketStatus marketStatus) {
        market.setMarketStatus(marketStatus);
    }

    private List<Market> filterMarketsNotInDb(MarketFetchResult marketFetchResult, List<Market> existMarkets) {
        Set<String> existMarketCodes = existMarkets.stream()
                .map(Market::getMarketCode)
                .collect(Collectors.toSet());
        return marketFetchResult.getMarkets().stream()
                .filter(m -> !existMarketCodes.contains(m.getMarketCode()))
                .toList();
    }

    private List<Market> filterMarketsInDbButNotInFetchResult(MarketFetchResult marketFetchResult, List<Market> existMarkets) {
        Set<String> fetchedMarketCodes = marketFetchResult.getMarkets().stream()
                .map(Market::getMarketCode)
                .collect(Collectors.toSet());
        return existMarkets.stream()
                .filter(existMarket -> !fetchedMarketCodes.contains(existMarket.getMarketCode())).toList();
    }
}
