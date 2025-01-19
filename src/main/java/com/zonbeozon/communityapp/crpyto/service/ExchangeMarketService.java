package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.repository.ExchangeMarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeMarketService {
    private final ExchangeMarketRepository exchangeMarketRepository;
    private final ExchangeService exchangeService;

    /**
     * add 호출 전에 중복 검사가 필요함.
     */
    public void add(Market market, Exchange exchange) {
        ExchangeMarket exchangeMarket = new ExchangeMarket(exchange, market);
        exchangeMarketRepository.save(exchangeMarket);
        market.getExchangeMarkets().add(exchangeMarket);
        exchange.getExchangeMarkets().add(exchangeMarket);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicate(Market market, Exchange exchange) {
        List<ExchangeMarket> exchangeMarkets = exchangeMarketRepository.findByExchangeJoinMarket(exchange);
        return exchangeMarkets.stream()
                .anyMatch(
                        exchangeMarket -> exchangeMarket.getMarket().getMarketCode()
                                .equals(market.getMarketCode())
                );
    }

    @Transactional(readOnly = true)
    public List<Market> getMarketsByExchange(Exchange exchange) {
        return exchangeMarketRepository.findByExchangeJoinMarket(exchange).stream()
                .map(ExchangeMarket::getMarket).toList();
    }

    @Transactional(readOnly = true)
    public List<Market> getMarketsByExchangeName(String exchangeName) {
        Exchange exchange = exchangeService.findByName(exchangeName);
        return getMarketsByExchange(exchange);
    }
}
