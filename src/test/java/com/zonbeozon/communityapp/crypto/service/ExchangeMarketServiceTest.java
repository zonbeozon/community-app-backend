package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.ExchangeMarket;
import com.zonbeozon.communityapp.crpyto.domain.exchangemarket.repository.ExchangeMarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.service.ExchangeMarketService;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yaml.snakeyaml.error.Mark;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeMarketServiceTest {
    @Mock
    private ExchangeMarketRepository mockExchangeMarketRepository;
    @Mock
    private ExchangeService mockExchangeService;
    @InjectMocks
    private ExchangeMarketService exchangeMarketService;
    private Market market_1;
    private Exchange exchange;
    private Market market_2;

    @BeforeEach
    void setUp() {
        createMarkets();
        createExchange();
    }

    @Test
    void 정상적으로_저장되어야_한다() {
        exchangeMarketService.add(market_1, exchange);

        verify(mockExchangeMarketRepository,times(1)).save(any(ExchangeMarket.class));
        Assertions.assertEquals(1, market_1.getExchangeMarkets().size());
        Assertions.assertEquals(1, exchange.getExchangeMarkets().size());
    }

    @Test
    void 거래소_영문명에_해당하는_거래소의_마켓들을_리턴한다() {
        ExchangeMarket exchangeMarket_1 = new ExchangeMarket(exchange, market_1);
        ExchangeMarket exchangeMarket_2 = new ExchangeMarket(exchange, market_2);

        when(mockExchangeService.findByName(exchange.getEnglishName())).thenReturn(exchange);
        when(mockExchangeMarketRepository.findByExchangeJoinMarket(exchange))
                .thenReturn(List.of(exchangeMarket_1, exchangeMarket_2));

        List<Market> market = exchangeMarketService.getMarketsByExchangeName(exchange.getEnglishName());

        Assertions.assertEquals(2, market.size());
        Assertions.assertEquals(List.of(market_1, market_2), market);
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

    private void createExchange() {
        exchange = ExampleObjectFactory.createExchange(
                "exampleExchange",
                "예시거래소",
                "예시거래소 입니다."
        );
    }
}
