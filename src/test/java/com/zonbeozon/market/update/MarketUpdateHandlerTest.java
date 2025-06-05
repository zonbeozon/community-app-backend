package com.zonbeozon.market.update;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.MarketFixture;
import com.zonbeozon.market.MarketTestUtils;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.fetch.MarketFetchData;
import com.zonbeozon.market.service.MarketEntityQueryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;


@ExtendWith(MockitoExtension.class)
public class MarketUpdateHandlerTest {
    @Mock
    private MarketEntityQueryService marketEntityQueryService;
    @InjectMocks
    private MarketUpdateHandler marketUpdateHandler;

    @Test
    @DisplayName("마켓을 업데이트 한다.")
    void updateMarket_updatesSignedChangeRate() {
        Market market = MarketFixture.ETH_KRW_UPBIT_MARKET;
        MarketTestUtils.setId(1L, market);
        market.updateSignedChangeRate(BigDecimal.ONE);
        BigDecimal newSignedChangeRate = BigDecimal.TEN;
        Mockito.when(marketEntityQueryService.getMarketByIdOrThrow(Mockito.eq(1L))).thenReturn(market);
        MarketFetchData marketFetchData = new MarketFetchData(
                market.getId(),
                FiatType.KRW,
                market.getMarketCode(),
                null,
                null,
                null,
                null,
                null,
                newSignedChangeRate,
                null
        );

        marketUpdateHandler.handle(marketFetchData);

        Assertions.assertThat(market.getSignedChangeRate()).isEqualTo(newSignedChangeRate);
    }
}
