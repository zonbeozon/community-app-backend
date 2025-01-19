package com.zonbeozon.communityapp.crypto.domain.market;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MarketTest {
    @Test
    void DTO로_부터_엔터티가_제대로_생성되어야_한다() {
        MarketRequest marketRequest = ExampleObjectFactory.createExampleMarketRequest(
                "BTC-USDT",
                "비트코인",
                "bitcoin"
        );
        Market market = Market.fromDto(marketRequest);

        Assertions.assertEquals(marketRequest.getEnglishName(), market.getEnglishName());
        Assertions.assertEquals(marketRequest.getKoreanName(), market.getKoreanName());
        Assertions.assertEquals(marketRequest.getMarketCode(), market.getMarketCode());
    }
}
