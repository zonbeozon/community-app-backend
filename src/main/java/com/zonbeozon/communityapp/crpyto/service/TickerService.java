package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketStatus;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.repository.TickerRepository;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.crpyto.exception.TickerException;
import com.zonbeozon.communityapp.crpyto.fetch.dto.TickerFetchResult;
import com.zonbeozon.communityapp.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TickerService {
    private final TickerRepository tickerRepository;
    private final ExchangeService exchangeService;
    private final ExchangeMarketService exchangeMarketService;

    public void updateTickers(TickerFetchResult tickerFetchResult) {
        Exchange exchange = exchangeService.findByName(tickerFetchResult.getExchangeName());
        List<Market> markets = exchangeMarketService.getMarketsWithTickersByExchange(exchange);
        if (markets.isEmpty()) throw new ExchangeException(ErrorCode.EMPTY_MARKET_EXCHANGE);
        Map<String, Ticker> marketCodeTickerMap = createMarketCodeTickerMap(tickerFetchResult.getTickers());
        markets.forEach(market -> updateTicker(market, marketCodeTickerMap, exchange));
    }

    private void updateTicker(Market market, Map<String, Ticker> marketCodeTickerMap, Exchange exchange) {
        if(market.getMarketStatus() == MarketStatus.DELISTED) return;
        Ticker oldTicker = market.getTicker();
        Ticker newTicker = Optional.ofNullable(marketCodeTickerMap.get(market.getMarketCode()))
                .orElseThrow(()-> new TickerException(ErrorCode.TICKER_NOT_FOUND));
        if(oldTicker == null) {
            newTicker.setExchange(exchange);
            tickerRepository.save(newTicker);
            market.setTicker(newTicker);
            return;
        }
        setAll(oldTicker, newTicker);
    }

    private Map<String, Ticker> createMarketCodeTickerMap(Collection<Ticker> tickers) {
        Map<String, Ticker> marketCodeTickerMap = new HashMap<>();
        tickers.forEach(ticker -> {
            marketCodeTickerMap.put(ticker.getMarketCode(), ticker);
        });
        return marketCodeTickerMap;
    }

    private void setAll(Ticker oldTicker, Ticker newTicker) {
        oldTicker.setAccTradePrice(newTicker.getAccTradePrice());
        oldTicker.setHighPrice(newTicker.getHighPrice());
        oldTicker.setLowPrice(newTicker.getLowPrice());
        oldTicker.setOpeningPrice(newTicker.getOpeningPrice());
        oldTicker.setSignedChangePrice(newTicker.getSignedChangePrice());
        oldTicker.setSignedChangeRate(newTicker.getSignedChangeRate());
        oldTicker.setTradePrice(newTicker.getTradePrice());
    }
}
