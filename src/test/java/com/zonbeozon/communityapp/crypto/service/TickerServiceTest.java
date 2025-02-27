package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.TickerPriceInfo;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerDto;
import com.zonbeozon.communityapp.crpyto.domain.ticker.repository.TickerRepository;
import com.zonbeozon.communityapp.crpyto.service.TickerService;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRate;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.service.ExchangeRateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TickerServiceTest {
    @Mock
    private TickerRepository tickerRepository;
    @Mock
    private ExchangeRateService exchangeRateService;
    @InjectMocks
    private TickerService tickerService;

    private final TickerDto tickerDto = createTickerDto();

    @BeforeEach
    void setUp() {
        when(exchangeRateService.getLatestExchangeRate(ExchangeRateCode.USD))
                .thenReturn(ExchangeRate.builder().rate(BigDecimal.TEN).build());

    }

    @Test
    @DisplayName("마켓에 티커 필드가 null이라면 새롭게 만들어서 저장한다.")
    void updateTicker_ShouldCreateAndSaveNewTicker_WhenTickerIsNull() {
        Market market = Market.builder().build();

        tickerService.updateTicker(market, tickerDto);

        Assertions.assertNotNull(market.getTicker());
        verify(tickerRepository, times(1)).save(any(Ticker.class));
    }

    @Test
    @DisplayName("마켓에 티커가 기존이 있다면 update메서드를 통해 변경한다.")
    void updateTicker_ShouldUpdateExistingTicker_WhenTickerExists() {
        Market market = Market.builder().build();
        Ticker ticker = createTicker();
        market.setTicker(ticker);

        tickerService.updateTicker(market, tickerDto);

        Assertions.assertEquals(tickerDto.signedChangeRate(), ticker.getSignedChangeRate());
        Assertions.assertEquals(tickerDto.openingPrice(), ticker.getTickerPriceInfoUsd().getOpeningPrice());
        Assertions.assertEquals(tickerDto.highPrice(), ticker.getTickerPriceInfoUsd().getHighPrice());
        Assertions.assertEquals(tickerDto.lowPrice(), ticker.getTickerPriceInfoUsd().getLowPrice());
        Assertions.assertEquals(tickerDto.tradePrice(), ticker.getTickerPriceInfoUsd().getTradePrice());
        Assertions.assertEquals(tickerDto.signedChangePrice(), ticker.getSignedChangeRate());
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

    private Ticker createTicker() {
        BigDecimal number = BigDecimal.ZERO;
        return Ticker.builder()
                .signedChangeRate(number)
                .tickerPriceInfoKrw(TickerPriceInfo.builder()
                        .lowPrice(number)
                        .accTradePrice(number)
                        .highPrice(number)
                        .tradePrice(number)
                        .openingPrice(number)
                        .signedChangePrice(number)
                        .build())
                .tickerPriceInfoUsd(TickerPriceInfo.builder()
                        .lowPrice(number)
                        .accTradePrice(number)
                        .highPrice(number)
                        .tradePrice(number)
                        .openingPrice(number)
                        .signedChangePrice(number)
                        .build())
                .build();
    }
}
