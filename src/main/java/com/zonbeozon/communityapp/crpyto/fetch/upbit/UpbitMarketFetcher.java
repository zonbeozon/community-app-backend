package com.zonbeozon.communityapp.crpyto.fetch.upbit;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.fetch.MarketFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.dto.MarketFetchResult;
import com.zonbeozon.communityapp.crpyto.fetch.upbit.dto.UpbitMarketRequest;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultFetcher;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpbitMarketFetcher implements MarketFetcher {
    private static final String UPBIT_MARKET_API_URL = "https://api.upbit.com/v1/market/all";
    private static final String exchangeName = "upbit";

    private final DefaultFetcher defaultFetcher;

    @Override
    public MarketFetchResult fetch() {
        List<UpbitMarketRequest> marketRequests = defaultFetcher.fetchWithParam(
                UPBIT_MARKET_API_URL,
                getParamMap(),
                new ParameterizedTypeReference<>() {});
        return convert(marketRequests);
    }

    public MarketFetchResult convert(List<UpbitMarketRequest> upbitMarketRequests) {
        MarketFetchResult marketFetchResult = new MarketFetchResult();
        List<Market> markets = upbitMarketRequests.stream()
                .map(r-> new MarketRequest(
                        r.getMarketCode(),
                        r.getKoreanName(),
                        r.getEnglishName()))
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
