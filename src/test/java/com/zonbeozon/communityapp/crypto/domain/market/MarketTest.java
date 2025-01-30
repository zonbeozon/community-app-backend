package com.zonbeozon.communityapp.crypto.domain.market;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MarketTest {
    @Test
    void DTO로_부터_엔터티가_제대로_생성되어야_한다() {
        MarketRequest marketRequest = MarketRequest.builder()
                .marketCode("BTC-USDT")
                .englishName("bitcoin")
                .koreanName("비트코인")
                .build();
        Market market = Market.fromDto(marketRequest);

        Assertions.assertEquals(marketRequest.englishName(), market.getEnglishName());
        Assertions.assertEquals(marketRequest.koreanName(), market.getKoreanName());
        Assertions.assertEquals(marketRequest.marketCode(), market.getMarketCode());
    }
}
