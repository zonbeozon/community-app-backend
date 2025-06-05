package com.zonbeozon.market.service;

import com.zonbeozon.common.DummyEntityValidator;
import com.zonbeozon.market.MarketFixture;
import com.zonbeozon.market.MarketTestUtils;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.repository.MarketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ContextConfiguration(classes = {MarketService.class, DummyEntityValidator.class})
public class MarketServiceTest {
    @Autowired
    MarketService marketService;
    @Autowired
    MarketRepository marketRepository;

    @Test
    @DisplayName("마켓 추가시 중복되는 마켓이 있으면 제외하고 추가한다.")
    void addMarket_shouldIgnoreDuplicateMarket_whenMarketAlreadyExists() {
        //마켓 중복은 동일 거래소 동일 마켓코드로 판단한다.
        Market market = MarketFixture.ETH_KRW_UPBIT_MARKET;
        marketService.addMarket(market);
        Market duplicateMarket = MarketTestUtils.copyMarket(market);
        marketService.addMarket(duplicateMarket);
        Assertions.assertThat(marketRepository.findAll()).hasSize(1);
    }

}
