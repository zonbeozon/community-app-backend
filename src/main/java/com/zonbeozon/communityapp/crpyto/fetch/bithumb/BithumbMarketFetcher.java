package com.zonbeozon.communityapp.crpyto.fetch.bithumb;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.MarketFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.bithumb.dto.BithumbMarketRequest;
import com.zonbeozon.communityapp.crpyto.fetch.dto.MarketFetchResult;
import com.zonbeozon.communityapp.crpyto.fetch.upbit.dto.UpbitMarketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BithumbMarketFetcher implements MarketFetcher {
    private static final String BITHUMB_MARKET_API_URL = "https://api.bithumb.com/v1/market/all";
    private static final String exchangeName = "bithumb";

    private final DefaultFetcher defaultFetcher;

    @Override
    public MarketFetchResult fetch() {
        List<BithumbMarketRequest> marketRequests = defaultFetcher.fetchWithParam(
                BITHUMB_MARKET_API_URL,
                getParamMap(),
                new ParameterizedTypeReference<>() {});
        return convert(marketRequests);
    }

    public MarketFetchResult convert(List<BithumbMarketRequest> bithumbMarketRequests) {
        MarketFetchResult marketFetchResult = new MarketFetchResult();
        List<Market> markets = bithumbMarketRequests.stream()
                .map(r-> MarketRequest.builder()
                        .marketCode(r.getMarketCode())
                        .koreanName(r.getKoreanName())
                        .englishName(r.getEnglishName())
                        .marketType(MarketType.fromPairString(r.getMarketCode(), false))
                        .build())
                .map(Market::fromDto).toList();
        marketFetchResult.setMarkets(markets);
        marketFetchResult.setExchangeName(exchangeName);

        return marketFetchResult;
    }

    private MultiValueMap<String, String> getParamMap() {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("is_details", "false");

        return paramMap;
    }
}
