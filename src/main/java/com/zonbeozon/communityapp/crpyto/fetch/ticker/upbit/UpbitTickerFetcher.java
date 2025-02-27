package com.zonbeozon.communityapp.crpyto.fetch.ticker.upbit;

import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.TickerFetcher;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerDto;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.upbit.dto.UpbitTickerRequest;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.service.market.MarketTypeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpbitTickerFetcher implements TickerFetcher {
    private static final String UPBIT_TICKER_API_URL = "https://api.upbit.com/v1/ticker";
    private static final String exchangeName = "upbit";

    private final DefaultRestFetcher defaultRestFetcher;
    private final MarketTypeResolver marketTypeResolver;

    @Override
    public TickerFetchResult fetch(Collection<String> marketCodes) {
        String joinedName = String.join(",", marketCodes);
        List<UpbitTickerRequest> tickerRequest = defaultRestFetcher.fetchWithParam(
                UPBIT_TICKER_API_URL,
                createParamMap(joinedName),
                new ParameterizedTypeReference<>() {});
        return convert(tickerRequest);
    }

    @Override
    public String getExchangeName() {
        return exchangeName;
    }

    private TickerFetchResult convert(List<UpbitTickerRequest> upbitTickerRequests) {
        List<TickerDto> tickers = upbitTickerRequests.stream()
                .map(r-> TickerDto.builder()
                        .marketType(marketTypeResolver.resolveFromMarketCode(r.marketCode()))
                        .marketCode(r.marketCode())
                        .openingPrice(r.openingPrice())
                        .highPrice(r.highPrice())
                        .lowPrice(r.lowPrice())
                        .tradePrice(r.tradePrice())
                        .signedChangePrice(r.signedChangePrice())
                        .signedChangeRate(r.signedChangeRate())
                        .accTradePrice(r.accTradePrice())
                        .build())
                .toList();

        return new TickerFetchResult(tickers);
    }

    private static MultiValueMap<String,String> createParamMap(String joinedName) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("markets", joinedName);
        return params;
    }
}
