package com.zonbeozon.communityapp.crypto.domain.exchangemarket.repository;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.repository.ExchangeRepository;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.repository.ExchangeMarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.market.repository.MarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.repository.TickerRepository;
import com.zonbeozon.communityapp.crpyto.service.ExchangeMarketService;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ExchangeMarketRepositoryTest {
    @Autowired
    private ExchangeMarketRepository exchangeMarketRepository;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private MarketRepository marketRepository;

    private Exchange exchange;
    private Market market_1;
    private Market market_2;
    private ExchangeMarket exchangeMarket_1;
    private ExchangeMarket exchangeMarket_2;
    private Ticker ticker_1;
    private Ticker ticker_2;
    @Autowired
    private TickerRepository tickerRepository;

    @BeforeEach
    void setUp() {
        exchange = ExampleObjectFactory.createExchange(
                "exampleExchange",
                "예시 거래소",
                "예시 거래소 입니다.");
        exchangeRepository.save(exchange);

        createMarkets();
        marketRepository.save(market_1);
        marketRepository.save(market_2);

        createExchangeMarkets();
        exchangeMarketRepository.save(exchangeMarket_1);
        exchangeMarketRepository.save(exchangeMarket_2);

        createTickers();
        ticker_1.setExchange(exchange);
        ticker_2.setExchange(exchange);
        tickerRepository.save(ticker_1);
        tickerRepository.save(ticker_2);
    }

    @Test
    void 마켓이_함께_페치조인되어야_한다() {
        List<ExchangeMarket> exchangeMarkets = exchangeMarketRepository.findByExchangeJoinMarket(exchange);

        exchangeMarkets.forEach(exchangeMarket -> {
            Assertions.assertTrue(Hibernate.isInitialized(exchangeMarket.getMarket()));
        });
    }

    @Test
    void 티커가_null인_Market의_ExchangeMarket도_포함되어야_한다() {
        List<ExchangeMarket> exchangeMarkets = exchangeMarketRepository.findByExchangeJoinMarketWithTicker(exchange);

        Assertions.assertEquals(2, exchangeMarkets.size());
        exchangeMarkets.forEach(exchangeMarket -> {
            Assertions.assertNull(exchangeMarket.getMarket().getTicker());
        });
    }

    @Test
    void 티커가_Market과_연관관계가_설정되어_있다면_티커까지_페치조인_되어야_한다() {
        market_1.setTicker(ticker_1);
        market_2.setTicker(ticker_2);
        List<ExchangeMarket> exchangeMarkets = exchangeMarketRepository.findByExchangeJoinMarketWithTicker(exchange);
        exchangeMarkets.forEach(exchangeMarket -> {
            Assertions.assertTrue(Hibernate.isInitialized(exchangeMarket.getMarket().getTicker()));
        });
    }

    private void createMarkets() {
        market_1 = ExampleObjectFactory.createMarket(
                "BTC-USDT",
                "비트코인",
                "bitcoin",
                MarketType.USDT
        );

        market_2 = ExampleObjectFactory.createMarket(
                "ETH-USDT",
                "이더리움",
                "ethereum",
                MarketType.USDT
        );
    }

    private void createExchangeMarkets() {
        exchangeMarket_1 = new ExchangeMarket(exchange, market_1);
        exchangeMarket_2 = new ExchangeMarket(exchange, market_2);
    }

    private void createTickers() {
        ticker_1 = ExampleObjectFactory.createTicker(
                "BTC-USDT",
                100.,
                100.,
                100.,
                100.,
                100.,
                100.,
                100.
        );
        ticker_2 = ExampleObjectFactory.createTicker(
                "ETH-USDT",
                100.,
                100.,
                100.,
                100.,
                100.,
                100.,
                100.
        );
    }
}
