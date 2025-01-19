package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.repository.TickerRepository;
import com.zonbeozon.communityapp.crpyto.fetch.dto.TickerFetchResult;
import lombok.RequiredArgsConstructor;
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
        List<Market> markets = exchangeMarketService.getMarketsByExchange(exchange);
        if (markets.isEmpty()) throw new RuntimeException("exchange name:" + exchange.getEnglishName() + "Market is empty");
        Map<String, Ticker> marketCodeTickerMap = createMarketCodeTickerMap(tickerFetchResult.getTickers());
        for (Market market : markets) {
            Ticker oldTicker = market.getTicker();
            Ticker newTicker = Optional.ofNullable(marketCodeTickerMap.get(market.getMarketCode()))
                    .orElseThrow(()-> new RuntimeException("해당하는 마켓 코드에 해당하는 티커가 없음"));
            if(oldTicker == null) {
                newTicker.setExchange(exchange);
                tickerRepository.save(newTicker);
                market.setTicker(newTicker);
                continue;
            }
            setAll(oldTicker, newTicker);
        }
    }

    private Map<String, Ticker> createMarketCodeTickerMap(Collection<Ticker> tickers) {
        Map<String, Ticker> marketCodeTickerMap = new HashMap<>();
        tickers.forEach(ticker -> {
            marketCodeTickerMap.put(ticker.getMarketCode(), ticker);
        });
        return marketCodeTickerMap;
    }

    private void setAll(Ticker oldTicker, Ticker newTicker) {
        oldTicker.setUpdatedAt(newTicker.getUpdatedAt());
        oldTicker.setAccTradePrice(newTicker.getAccTradePrice());
        oldTicker.setHighPrice(newTicker.getHighPrice());
        oldTicker.setLowPrice(newTicker.getLowPrice());
        oldTicker.setOpeningPrice(newTicker.getOpeningPrice());
        oldTicker.setSignedChangePrice(newTicker.getSignedChangePrice());
        oldTicker.setSignedChangeRate(newTicker.getSignedChangeRate());
        oldTicker.setTradePrice(newTicker.getTradePrice());
    }
}
