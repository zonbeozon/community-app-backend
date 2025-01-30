package com.zonbeozon.communityapp.crypto.domain.ticker;

import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerRequest;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TickerTest {
    @Test
    void DTO로_부터_엔터티가_제대로_생성되어야_한다() {
        TickerRequest tickerRequest = TickerRequest.builder()
                .marketCode("BTC-USDT")
                .highPrice(100.)
                .lowPrice(100.)
                .openingPrice(100.)
                .tradePrice(100.)
                .accTradePrice(100.)
                .signedChangeRate(10.)
                .signedChangePrice(1.)
                .build();

        Ticker ticker = Ticker.fromDto(tickerRequest);

        Assertions.assertEquals(tickerRequest.marketCode(), ticker.getMarketCode());
        Assertions.assertEquals(tickerRequest.highPrice(), ticker.getHighPrice());
        Assertions.assertEquals(tickerRequest.lowPrice(), ticker.getLowPrice());
        Assertions.assertEquals(tickerRequest.openingPrice(), ticker.getOpeningPrice());
        Assertions.assertEquals(tickerRequest.tradePrice(), ticker.getTradePrice());
        Assertions.assertEquals(tickerRequest.accTradePrice(), ticker.getAccTradePrice());
        Assertions.assertEquals(tickerRequest.signedChangeRate(), ticker.getSignedChangeRate());
        Assertions.assertEquals(tickerRequest.signedChangePrice(), ticker.getSignedChangePrice());

    }
}
