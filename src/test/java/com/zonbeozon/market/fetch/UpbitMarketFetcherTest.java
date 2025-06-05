package com.zonbeozon.market.fetch;

import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.MarketFixture;
import com.zonbeozon.market.MarketHolder;
import com.zonbeozon.market.MarketTestUtils;
import com.zonbeozon.market.SimpleMarketHolder;
import com.zonbeozon.market.entity.Market;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class UpbitMarketFetcherTest extends AbstractMarketFetcherTest {
    private static final String REQUEST_URI = "https://api.upbit.com/v1/ticker";
    private static final RestClient.Builder restClientBuilder = RestClient.builder();
    private static final MockMarketServer mockMarketServer = new MockMarketServer(
            restClientBuilder,
            REQUEST_URI,
            "markets",
            "KRW-BTC,KRW-ETH",
            createBinanceMarketFetchResponse()
    );
    private static final MarketFetchDataMapper marketFetchDataMapper = new MarketFetchDataMapper();
    private static final UpbitMarketFetcher upbitMarketFetcher = new UpbitMarketFetcher(restClientBuilder);
    private static final Market BTC_KRW_UPBIT_MARKET = MarketFixture.BTC_KRW_UPBIT_MARKET;
    private static final Market ETH_KRW_UPBIT_MARKET = MarketFixture.ETH_KRW_UPBIT_MARKET;
    static {
        MarketTestUtils.setId(1L, BTC_KRW_UPBIT_MARKET);
        MarketTestUtils.setId(2L, ETH_KRW_UPBIT_MARKET);
    }
    //MockServer의 paramValue 체크를 위해서는 순서가 보장되어야 되기 때문에 LinkedHashSet사용
    private static final MarketHolder marketHolder = new SimpleMarketHolder(new LinkedHashSet<>(List.of(BTC_KRW_UPBIT_MARKET, ETH_KRW_UPBIT_MARKET)));

    public UpbitMarketFetcherTest() {
        super(upbitMarketFetcher);
    }

    private static List<MarketFetchResponse> createBinanceMarketFetchResponse() {
        return List.of(
                new UpbitMarketFetchResponse("KRW-BTC", BigDecimal.valueOf(120), BigDecimal.valueOf(130),
                        BigDecimal.valueOf(110), BigDecimal.valueOf(125), BigDecimal.valueOf(5),
                        BigDecimal.valueOf(4.2), BigDecimal.valueOf(1000000)),
                new UpbitMarketFetchResponse("KRW-ETH", BigDecimal.valueOf(2200), BigDecimal.valueOf(2300),
                        BigDecimal.valueOf(2100), BigDecimal.valueOf(2250), BigDecimal.valueOf(50),
                        BigDecimal.valueOf(2.3), BigDecimal.valueOf(500000))
        );
    }

    @Override
    protected MarketHolder getMarketHolder() {
        return marketHolder;
    }

    @Override
    protected List<MarketFetchData> expectedReturn() {
        return marketFetchDataMapper.map(marketHolder, createBinanceMarketFetchResponse(), FiatType.KRW);
    }

}
