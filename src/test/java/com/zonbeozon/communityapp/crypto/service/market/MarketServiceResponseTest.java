package com.zonbeozon.communityapp.crypto.service.market;

import com.zonbeozon.communityapp.crpyto.controller.dto.market.EntireMarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MiniMarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.ticker.TickerResponse;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.market.repository.MarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.TickerPriceInfo;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.market.MarketService;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketServiceResponseTest {
    @Mock
    private MarketRepository marketRepository;
    @Mock
    private ExchangeService exchangeService;
    @InjectMocks
    private MarketService marketService;

    private final Exchange exchange = createExchange();
    private final Ticker ticker = createTicker();
    private final Market market = createMarket();

    @Test
    @DisplayName("단일 currency 마켓 조회시 원하는 krw fiat 요청이 온다면 원화 기준 dto를 만든다.")
    void shouldReturnKrwBasedMarketInfoWhenKrwFiatRequested() {
        when(marketRepository.findActiveMarketsWithTickerAndExchangeByCurrencyId(1L))
                .thenReturn(List.of(market));

        MarketInfoResponse result = marketService.getMarketInfoByCurrency(1L, FiatType.KRW);

        Assertions.assertEquals(1L, result.currencyId());
        Assertions.assertEquals(1, result.markets().size());
        TickerResponse tickerResponse = result.markets().get("ex_1").get("usdt");
        Assertions.assertEquals(ticker.getTickerPriceInfoKrw().getAccTradePrice(), tickerResponse.accTradePrice());
        Assertions.assertEquals(ticker.getTickerPriceInfoKrw().getHighPrice(), tickerResponse.highPrice());
        Assertions.assertEquals(ticker.getTickerPriceInfoKrw().getLowPrice(), tickerResponse.lowPrice());
        Assertions.assertEquals(ticker.getTickerPriceInfoKrw().getTradePrice(), tickerResponse.tradePrice());
        Assertions.assertEquals(ticker.getTickerPriceInfoKrw().getOpeningPrice(), tickerResponse.openingPrice());
        Assertions.assertEquals(ticker.getTickerPriceInfoKrw().getSignedChangePrice(), tickerResponse.signedChangePrice());
    }

    @Test
    @DisplayName("거래소 기준 전체 마켓 조회시 usd fiat 요청이라면 usd 기준 dto를 만든다.")
    void shouldReturnUsdBasedMarketInfoWhenUsdFiatRequested() {
        when(marketRepository.findActiveMarketsWithTickerAndCurrencyByExchangeAndMarketType(1L, MarketType.USDT))
                .thenReturn(Collections.singletonList(market));
        when(exchangeService.findById(1L)).thenReturn(exchange);

        EntireMarketInfoResponse entireMarketInfoResponse = marketService.getEntireMarketsByExchange(1L, FiatType.USD);
        Assertions.assertEquals(1, entireMarketInfoResponse.size());
        MiniMarketInfoResponse miniMarketInfoResponse = entireMarketInfoResponse.currencies().getFirst();
        Assertions.assertEquals(1L, miniMarketInfoResponse.currencyId());
        Assertions.assertEquals(ticker.getTickerPriceInfoUsd().getTradePrice(), miniMarketInfoResponse.tradePrice());
    }

    private Ticker createTicker() {
        BigDecimal krwPrice = BigDecimal.TEN;
        BigDecimal usdPrice = krwPrice.multiply(new BigDecimal("1300"));

         return Ticker.builder()
                .signedChangeRate(BigDecimal.ZERO)
                .tickerPriceInfoKrw(TickerPriceInfo.builder()
                        .lowPrice(krwPrice)
                        .accTradePrice(krwPrice)
                        .highPrice(krwPrice)
                        .tradePrice(krwPrice)
                        .openingPrice(krwPrice)
                        .signedChangePrice(krwPrice)
                        .build())
                .tickerPriceInfoUsd(TickerPriceInfo.builder()
                        .lowPrice(usdPrice)
                        .accTradePrice(usdPrice)
                        .highPrice(usdPrice)
                        .tradePrice(usdPrice)
                        .openingPrice(usdPrice)
                        .signedChangePrice(usdPrice)
                        .build())
                .build();
    }

    private Market createMarket() {
        Market market = Market.builder()
                .exchange(exchange)
                .marketType(MarketType.USDT)
                .currency(createCurrency())
                .build();
        market.setTicker(ticker);
        return market;
    }

    private Currency createCurrency() {
        Currency currency = mock(Currency.class);
        when(currency.getId()).thenReturn(1L);
        return currency;
    }

    private Exchange createExchange() {
        Exchange exchange = mock(Exchange.class);
        when(exchange.getId()).thenReturn(1L);
        when(exchange.getTopPriorityMarketType()).thenReturn(MarketType.USDT);
        when(exchange.getEnglishName()).thenReturn("ex_1");
        return exchange;
    }
}
