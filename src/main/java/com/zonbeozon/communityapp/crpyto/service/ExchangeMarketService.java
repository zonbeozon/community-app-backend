package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.repository.ExchangeMarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeMarketException;
import com.zonbeozon.communityapp.exception.ErrorCode;
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

    public void add(Market market, Exchange exchange) {
        ExchangeMarket exchangeMarket = new ExchangeMarket(exchange, market);
        exchangeMarketRepository.save(exchangeMarket);
        market.getExchangeMarkets().add(exchangeMarket);
        exchange.getExchangeMarkets().add(exchangeMarket);
    }

    public void delete(Market market, Exchange exchange) {
        ExchangeMarket exchangeMarket = exchangeMarketRepository.findByExchangeAndMarket(exchange, market)
                .orElseThrow(() -> new ExchangeMarketException(ErrorCode.EXCHANGE_MARKET_NOT_FOUND));

        exchangeMarketRepository.delete(exchangeMarket);
    }

    @Transactional(readOnly = true)
    public List<Market> getMarketsByExchange(Exchange exchange) {
        return exchangeMarketRepository.findByExchangeJoinMarket(exchange).stream()
                .map(ExchangeMarket::getMarket).toList();
    }

    @Transactional(readOnly = true)
    public List<Market> getMarketsWithTickersByExchange(Exchange exchange) {
        return exchangeMarketRepository.findByExchangeJoinMarketWithTicker(exchange).stream()
                .map(ExchangeMarket::getMarket).toList();
    }

    @Transactional(readOnly = true)
    public List<Market> getMarketsByExchangeName(String exchangeName) {
        Exchange exchange = exchangeService.findByName(exchangeName);
        return getMarketsByExchange(exchange);
    }
}
