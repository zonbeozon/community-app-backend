package com.zonbeozon.communityapp.crpyto.fetch.ticker.bithumb.strategy.rest;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerDto;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.TickerFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.bithumb.dto.BithumbTickerRequest;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.service.market.MarketService;
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
public class BithumbTickerFetcher implements TickerFetcher {
    private static final String BITHUMB_TICKER_API_URL = "https://api.bithumb.com/v1/ticker";
    private static final String exchangeName = "bithumb";

    private final DefaultRestFetcher defaultRestFetcher;
    private final MarketTypeResolver marketTypeResolver;

    @Override
    public TickerFetchResult fetch(Collection<String> marketCodes) {
        List<BithumbTickerRequest> tickerRequest = defaultRestFetcher.fetchWithParam(
                BITHUMB_TICKER_API_URL,
                createParamMap(marketCodes),
                new ParameterizedTypeReference<>() {});
        return convert(tickerRequest);
    }

    @Override
    public String getExchangeName() {
        return exchangeName;
    }

    public TickerFetchResult convert(List<BithumbTickerRequest> bithumbTickerRequests) {

        List<TickerDto> tickers = bithumbTickerRequests.stream()
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

    private static MultiValueMap<String,String> createParamMap(Collection<String> marketCodes) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        String joinedName = String.join(",", marketCodes);
        params.add("markets", joinedName);
        return params;
    }
}
