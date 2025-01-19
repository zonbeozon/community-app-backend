package com.zonbeozon.communityapp.crypto.domain.ticker;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerRequest;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TickerTest {
    @Test
    void DTO로_부터_엔터티가_제대로_생성되어야_한다() {
        TickerRequest tickerRequest = ExampleObjectFactory.createExampleTickerRequest(
                "BTC-USDT",
                1000.,
                1000.,
                1000.,
                1000.,
                1000.,
                10.,
                1000.,
                LocalDateTime.of(2000,1,1,0,0,0)
        );

        Ticker ticker = Ticker.fromDto(tickerRequest);

        Assertions.assertEquals(tickerRequest.getMarketCode(), ticker.getMarketCode());
        Assertions.assertEquals(tickerRequest.getHighPrice(), ticker.getHighPrice());
        Assertions.assertEquals(tickerRequest.getLowPrice(), ticker.getLowPrice());
        Assertions.assertEquals(tickerRequest.getOpeningPrice(), ticker.getOpeningPrice());
        Assertions.assertEquals(tickerRequest.getTradePrice(), ticker.getTradePrice());
        Assertions.assertEquals(tickerRequest.getAccTradePrice(), ticker.getAccTradePrice());
        Assertions.assertEquals(tickerRequest.getSignedChangeRate(), ticker.getSignedChangeRate());
        Assertions.assertEquals(tickerRequest.getSignedChangePrice(), ticker.getSignedChangePrice());
        Assertions.assertEquals(tickerRequest.getUpdatedAt(), ticker.getUpdatedAt());

    }
}
