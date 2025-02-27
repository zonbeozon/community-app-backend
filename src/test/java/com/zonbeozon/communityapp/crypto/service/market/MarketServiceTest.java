package com.zonbeozon.communityapp.crypto.service.market;

import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketRequest;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketStatus;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.market.repository.MarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerDto;
import com.zonbeozon.communityapp.crpyto.exception.MarketException;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.TickerFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.TickerService;
import com.zonbeozon.communityapp.crpyto.service.market.MarketCodeResolver;
import com.zonbeozon.communityapp.crpyto.service.market.MarketService;
import com.zonbeozon.communityapp.crpyto.service.market.MarketTypeResolver;
import com.zonbeozon.communityapp.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketServiceTest {
    @Mock
    private MarketRepository marketRepository;
    @Mock
    private ExchangeService exchangeService;
    @Mock
    private MarketCodeResolver marketCodeResolver;
    @Mock
    private MarketTypeResolver marketTypeResolver;
    @Mock
    private TickerService tickerService;
    @Mock
    private TickerFetcher tickerFetcher;

    private MarketService marketService;

    private final Exchange exchange_1 = createExchange(1L, "ex_1");
    private final Exchange exchange_2 = createExchange(2L, "ex_2");

    private final Currency currency = Currency.builder()
            .symbol("DUMMY")
            .build();

    @BeforeEach
    void setup() {
        marketService = new MarketService(
                marketRepository,
                marketTypeResolver,
                marketCodeResolver,
                exchangeService,
                tickerService,
                List.of(tickerFetcher)
        );
    }

    @Test
    @DisplayName("마켓 추가시 동일한 요청에서 동일한 마켓코드를 가졌지만 거래소가 다르다면 추가되어야 한다")
    void shouldAddMarketsWhenSameMarketCodeExistsInDifferentExchanges() {
        when(exchangeService.findByName(anyString())).thenAnswer(invocation -> {
            String exchangeName = invocation.getArgument(0);
            if(exchangeName.equals("ex_1")) {
                return exchange_1;
            }
            if(exchangeName.equals("ex_2")) {
                return exchange_2;
            }
            throw new RuntimeException("test failed");
        });
        when(marketRepository.findAll()).thenReturn(new ArrayList<>());
        when(marketCodeResolver.resolve(anyString(),anyString(),any(MarketType.class))).thenReturn("dummy");
        when(marketTypeResolver.resolveFromPair(anyString(), anyString())).thenReturn(MarketType.KRW);
        when(marketRepository.saveAll(anyCollection())).thenAnswer(invocation -> invocation.getArgument(0));
        List<MarketRequest> mqs = new ArrayList<>();
        mqs.add(new MarketRequest("ex_1", List.of("krw")));
        mqs.add(new MarketRequest("ex_2", List.of("krw")));

        marketService.addMarkets(mqs, currency);

        ArgumentCaptor<List<Market>> marketCaptor = ArgumentCaptor.forClass(List.class);
        verify(marketRepository, times(1)).saveAll(marketCaptor.capture());

        List<Market> savedMarkets = marketCaptor.getValue();
        Assertions.assertEquals(2, savedMarkets.size());
    }

    @Test
    @DisplayName("마켓 추가시 동일한 마켓코드 동일한 거래소라면 예외를 발생시킨다")
    void shouldThrowExceptionWhenSameMarketCodeExistsInSameExchange() {
        when(marketRepository.findAll()).thenReturn(new ArrayList<>());
        when(exchangeService.findByName("ex_1")).thenReturn(exchange_1);
        when(marketCodeResolver.resolve(anyString(),anyString(),any(MarketType.class))).thenReturn("dummy");
        when(marketTypeResolver.resolveFromPair(anyString(), anyString())).thenReturn(MarketType.KRW);
        List<MarketRequest> mqs = new ArrayList<>();
        mqs.add(new MarketRequest("ex_1", List.of("krw")));
        mqs.add(new MarketRequest("ex_1", List.of("krw")));

        MarketException marketException = Assertions.assertThrows(MarketException.class,
                ()-> marketService.addMarkets(mqs, currency));
        Assertions.assertEquals(ErrorCode.DUPLICATE_MARKET, marketException.getErrorCode());
    }

    @Test
    @DisplayName("마켓 추가시 이미 db에 동일한 마켓 코드, 동일한 거래소가 있다면 예외를 발생시킨다")
    void shouldThrowExceptionWhenDuplicateMarketExistsInDatabase() {
        when(exchangeService.findByName("ex_1")).thenReturn(exchange_1);
        when(marketRepository.findAll()).thenReturn(Collections.singletonList(Market.builder()
                .marketCode("dummy")
                .exchange(exchange_1)
                .build()));
        when(marketCodeResolver.resolve(anyString(),anyString(),any(MarketType.class))).thenReturn("dummy");
        when(marketTypeResolver.resolveFromPair(anyString(), anyString())).thenReturn(MarketType.KRW);
        List<MarketRequest> mqs = new ArrayList<>();
        mqs.add(new MarketRequest("ex_1", List.of("krw")));

        MarketException marketException = Assertions.assertThrows(MarketException.class,
                ()-> marketService.addMarkets(mqs, currency));
        Assertions.assertEquals(ErrorCode.DUPLICATE_MARKET, marketException.getErrorCode());
    }

    @Test
    @DisplayName("마켓 삭제시 마켓 id를 찾을 수 없다면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenDeletingNonExistentMarket() {
        when(marketRepository.findById(eq(1L))).thenReturn(Optional.empty());

        MarketException marketException = Assertions.assertThrows(MarketException.class,
                ()-> marketService.deleteMarket(1L));
        Assertions.assertEquals(ErrorCode.MARKET_NOT_FOUND, marketException.getErrorCode());
    }

    @Test
    @DisplayName("마켓 상태 변경시 마켓 id를 찾을 수 없다면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenChangingStatusOfNonExistentMarket() {
        when(marketRepository.findById(eq(1L))).thenReturn(Optional.empty());

        MarketException marketException = Assertions.assertThrows(MarketException.class,
                ()-> marketService.changeMarketStatus(1L, "active"));
        Assertions.assertEquals(ErrorCode.MARKET_NOT_FOUND, marketException.getErrorCode());
    }

    @Test
    @DisplayName("마켓 상태 변경시 요청이 동일 상태라면 메서드를 종료한다")
    void shouldNotChangeMarketStatusWhenStatusIsSame() {
        Market market = mock(Market.class);
        when(market.getMarketStatus()).thenReturn(MarketStatus.INACTIVE);
        when(marketRepository.findById(eq(1L))).thenReturn(Optional.of(market));

        marketService.changeMarketStatus(1L, "inactive");

        verify(market, never()).updateMarketStatus(any());
    }

    @Test
    @DisplayName("마켓 상태 변경시 존재하지 않는 마켓 상태라면 예외를 던진다.")
    void shouldThrowExceptionWhenInvalidMarketStatusRequested() {
        Market market = mock(Market.class);
        when(marketRepository.findById(eq(1L))).thenReturn(Optional.of(market));
        MarketException marketException = Assertions.assertThrows(MarketException.class,
                ()-> marketService.changeMarketStatus(1L, "notExistMarketStatus"));
        Assertions.assertEquals(ErrorCode.ILLEGAL_MARKET_STATUS, marketException.getErrorCode());
    }

    @Test
    @DisplayName("마켓 상태 변경시 inactive 상태일때 active 상태 변경을 요청하면 active 상태로 변경한다.")
    void shouldChangeMarketStatusFromInactiveToActive() {
        Market market = Market.builder().build();
        market.updateMarketStatus(MarketStatus.INACTIVE);
        when(marketRepository.findById(eq(1L))).thenReturn(Optional.of(market));
        marketService.changeMarketStatus(1L, "active");
        Assertions.assertEquals(MarketStatus.ACTIVE, market.getMarketStatus());
    }

    @Test
    @DisplayName("마켓 업데이트시 fetcher를 통해 fetch하고 마켓의 ticker필드를 업데이트한다.")
    void updateMarkets_ShouldFetchAndUpdateTicker() {
        Market market = createMarket();
        TickerDto tickerDto = createTickerDto();
        List<Market> markets = Collections.singletonList(market);
        TickerFetchResult fetchResult = new TickerFetchResult(Collections.singletonList(tickerDto));

        when(tickerFetcher.getExchangeName()).thenReturn("ex_1");
        when(exchangeService.findByName("ex_1")).thenReturn(exchange_1);
        when(marketRepository.findActiveMarketsWithTickerByExchange(exchange_1)).thenReturn(markets);
        when(tickerFetcher.fetch(anyCollection())).thenReturn(fetchResult);

        marketService.updateMarkets();

        verify(tickerService, times(1)).updateTicker(market, tickerDto);
    }

    private Market createMarket() {
        return Market.builder()
                .marketCode("BTCUSDT")
                .marketType(MarketType.USDT)
                .build();
    }

    private TickerDto createTickerDto() {
        BigDecimal number = BigDecimal.TEN;
        return TickerDto.builder()
                .marketType(MarketType.USDT)
                .marketCode("BTCUSDT")
                .highPrice(number)
                .accTradePrice(number)
                .openingPrice(number)
                .lowPrice(number)
                .signedChangePrice(number)
                .signedChangeRate(number)
                .tradePrice(number)
                .build();
    }

    private Exchange createExchange(long id, String name) {
        Exchange exchange = mock(Exchange.class);
        when(exchange.getEnglishName()).thenReturn(name);
        when(exchange.getId()).thenReturn(id);
        return exchange;
    }
}
