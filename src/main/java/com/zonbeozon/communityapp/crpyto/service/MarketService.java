package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
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

    public void addMarket(Market market, Exchange exchange) {
        if(exchangeMarketService.isDuplicate(market, exchange)) return;
        marketRepository.save(market);
        exchangeMarketService.add(market, exchange);
    }

    public void addMarkets(MarketFetchResult marketFetchResult) {
        Exchange exchange = exchangeService.findByName(marketFetchResult.getExchangeName());
        Set<String> marketCodes = exchangeMarketService.getMarketsByExchange(exchange).stream()
                .map(Market::getMarketCode)
                .collect(Collectors.toSet());
        List<Market> marketsNotInDb = marketFetchResult.getMarkets().stream()
                .filter(m -> !marketCodes.contains(m.getMarketCode()))
                .toList();
        if(marketsNotInDb.isEmpty()) return;
        marketRepository.saveAll(marketsNotInDb);
        marketsNotInDb.forEach(market-> exchangeMarketService.add(market, exchange));
    }
}
