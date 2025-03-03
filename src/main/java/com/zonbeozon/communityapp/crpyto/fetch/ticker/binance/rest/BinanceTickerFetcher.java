package com.zonbeozon.communityapp.crpyto.fetch.ticker.binance.rest;

import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerDto;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.TickerFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.binance.dto.BinanceTickerRequest;
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
public class BinanceTickerFetcher implements TickerFetcher {
    private static final String BINANCE_TICKER_API_URL = "https://api.binance.com/api/v3/ticker/tradingDay";
    private static final String exchangeName = "binance";

    private final DefaultRestFetcher defaultRestFetcher;
    private final MarketTypeResolver marketTypeResolver;

    @Override
    public TickerFetchResult fetch(Collection<String> marketCodes) {
        List<BinanceTickerRequest> tickerRequest = defaultRestFetcher.fetchWithParam(
                BINANCE_TICKER_API_URL,
                createParamMap(createJoinedCodeFromMarketCode(marketCodes)),
                new ParameterizedTypeReference<>() {});
        return convert(tickerRequest);
    }

    @Override
    public String getExchangeName() {
        return exchangeName;
    }

    private TickerFetchResult convert(List<BinanceTickerRequest> binanceTickerRequests) {
        List<TickerDto> tickers = binanceTickerRequests.stream()
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

    private static String createJoinedCodeFromMarketCode(Collection<String> marketCodes) {
        return "[\"" + String.join("\",\"", marketCodes) + "\"]";
    }

    private static MultiValueMap<String,String> createParamMap(String joinedCode) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("symbols", joinedCode);
        return params;
    }
}
