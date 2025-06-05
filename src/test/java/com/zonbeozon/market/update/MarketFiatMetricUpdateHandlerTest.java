package com.zonbeozon.market.update;

import com.zonbeozon.common.DummyEntityValidator;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.MarketFiatMetricTestUtils;
import com.zonbeozon.market.MarketFixture;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.fetch.MarketFetchData;
import com.zonbeozon.market.repository.MarketFiatMetricRepository;
import com.zonbeozon.market.repository.MarketRepository;
import com.zonbeozon.market.service.MarketFiatMetricService;
import com.zonbeozon.market.service.MarketService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

@DataJpaTest
@ContextConfiguration(classes = {
        MarketFiatMetricUpdateHandler.class,
        MarketService.class,
        MarketFiatMetricService.class,
        DummyEntityValidator.class
})
public class MarketFiatMetricUpdateHandlerTest {
    @Autowired
    private MarketFiatMetricUpdateHandler marketFiatMetricUpdateHandler;
    @Autowired
    private MarketRepository marketRepository;
    @Autowired
    private MarketFiatMetricRepository marketFiatMetricRepository;

    @Test
    @DisplayName("동일 마켓에 이미 동일 FiatType의 MarketFiatMetric이 존재하면 업데이트한다.")
    void updates_when_same_fiat_type_exists() {
        Market market = marketRepository.save(MarketFixture.ETH_KRW_UPBIT_MARKET);
        BigDecimal oldValue = BigDecimal.ONE;
        BigDecimal newValue = BigDecimal.TEN;
        MarketFiatMetric marketFiatMetric = MarketFiatMetricTestUtils.createMarketFiatMetricWithOneValue(
                market, FiatType.KRW, oldValue
        );
        marketFiatMetricRepository.save(marketFiatMetric);
        MarketFetchData marketFetchData = new MarketFetchData(
                market.getId(),
                FiatType.KRW,
                market.getMarketCode(),
                newValue,
                newValue,
                newValue,
                newValue,
                newValue,
                newValue,
                newValue
        );

        marketFiatMetricUpdateHandler.handle(marketFetchData);
        //업데이트 확인
        assertMarketFiatMetricEquals(marketFiatMetric, newValue);
        //추가되면 안된다.
        Assertions.assertThat(marketFiatMetricRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("동일 마켓이지만 FiatType이 다르면 MarketFiatMetric을 추가한다.")
    void adds_new_metric_when_fiat_type_differs() {
        Market market = marketRepository.save(MarketFixture.ETH_KRW_UPBIT_MARKET);
        BigDecimal oldValue = BigDecimal.ONE;
        BigDecimal newValue = BigDecimal.TEN;
        MarketFiatMetric marketFiatMetric = MarketFiatMetricTestUtils.createMarketFiatMetricWithOneValue(
                market, FiatType.KRW, oldValue
        );
        marketFiatMetricRepository.save(marketFiatMetric);
        MarketFetchData marketFetchData = new MarketFetchData(
                market.getId(),
                FiatType.USD,
                market.getMarketCode(),
                newValue,
                newValue,
                newValue,
                newValue,
                newValue,
                newValue,
                newValue
        );

        marketFiatMetricUpdateHandler.handle(marketFetchData);
        //기존 marketFiatMetric은 업데이트 되면 안된다.
        assertMarketFiatMetricEquals(marketFiatMetric, oldValue);
        //marketFiatMetric 추가됌
        Assertions.assertThat(marketFiatMetricRepository.findAll()).hasSize(2);
    }

    private void assertMarketFiatMetricEquals(MarketFiatMetric marketFiatMetric, BigDecimal value) {
        Assertions.assertThat(marketFiatMetric.getOpeningPrice()).isEqualTo(value);
        Assertions.assertThat(marketFiatMetric.getHighPrice()).isEqualTo(value);
        Assertions.assertThat(marketFiatMetric.getLowPrice()).isEqualTo(value);
        Assertions.assertThat(marketFiatMetric.getSignedChangePrice()).isEqualTo(value);
        Assertions.assertThat(marketFiatMetric.getAccTradePrice()).isEqualTo(value);
        Assertions.assertThat(marketFiatMetric.getTradePrice()).isEqualTo(value);
    }
}
