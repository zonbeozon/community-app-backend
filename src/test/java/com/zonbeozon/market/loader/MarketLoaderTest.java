package com.zonbeozon.market.loader;

import com.zonbeozon.market.service.MarketCodeResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
public class MarketLoaderTest {
    private static final String FILE_PATH = "/test-market_load.json";
    private static final String UPBIT_BTC_MARKET_CODE = "KRW-BTC";
    private static final String UPBIT_ETH_MARKET_CODE = "KRW-ETG";
    private static final String BITHUMB_BTC_MARKET_CODE = "KRW-BTC";
    private static final String BITHUMB_ETH_MARKET_CODE = "KRW-ETH";
    private static final String BINANCE_BTC_MARKET_CODE = "BTCUSDT";
    private static final String BINANCE_ETH_MARKET_CODE = "ETHUSDT";
    private MarketLoader marketLoader = new JsonMarketLoader(new MarketCodeResolver(), FILE_PATH, MarketRegistryHolder::new);

    @Test
    @DisplayName("json 파일로 부터 정상적으로 MarketRegistry를 만들어야 한다")
    void shouldLoadMarketRegistryFromJsonFile() {
        MarketRegistryHolder registryHolder = marketLoader.load();
        List<MarketRegistry> marketRegistries = new ArrayList<>();
        registryHolder.applyAll(marketRegistries::add);
        assertThat(marketRegistries)
                .hasSize(6)// 업비트 2개 + 빗썸 2개 + 바이낸스 2개 = 총 6개
                .extracting(MarketRegistry::getMarketCode)
                .containsExactlyInAnyOrder(
                        UPBIT_ETH_MARKET_CODE,
                        UPBIT_BTC_MARKET_CODE,
                        BINANCE_ETH_MARKET_CODE,
                        BINANCE_BTC_MARKET_CODE,
                        BITHUMB_BTC_MARKET_CODE,
                        BITHUMB_ETH_MARKET_CODE
                );
    }
}
