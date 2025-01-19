package com.zonbeozon.communityapp.crpyto.fetch.upbit;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerRequest;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.MarketFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.TickerFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.dto.MarketFetchResult;
import com.zonbeozon.communityapp.crpyto.fetch.upbit.dto.UpbitTickerRequest;
import com.zonbeozon.communityapp.crpyto.fetch.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.service.ExchangeMarketService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpbitTickerFetcher implements TickerFetcher {
    private static final String UPBIT_TICKER_API_URL = "https://api.upbit.com/v1/ticker";
    private static final String exchangeName = "upbit";

    private final ExchangeMarketService exchangeMarketService;
    private final DefaultFetcher defaultFetcher;

    @Override
    public TickerFetchResult fetch() {
        List<String> marketCodes = exchangeMarketService.getMarketsByExchangeName(exchangeName).stream()
                .map(Market::getMarketCode)
                .toList();
        if(marketCodes.isEmpty()) throw new RuntimeException("market not found");
        String joinedName = String.join(",", marketCodes);
        List<UpbitTickerRequest> tickerRequest = defaultFetcher.fetchWithParam(
                UPBIT_TICKER_API_URL,
                createParamMap(joinedName),
                new ParameterizedTypeReference<>() {});
        return convert(tickerRequest);
    }

    public TickerFetchResult convert(List<UpbitTickerRequest> upbitTickerRequests) {
        TickerFetchResult result = new TickerFetchResult();

        List<Ticker> tickers = upbitTickerRequests.stream()
                .map(r->new TickerRequest(
                        r.getMarketCode(),
                        r.getOpeningPrice(),
                        r.getHighPrice(),
                        r.getLowPrice(),
                        r.getTradePrice(),
                        r.getSignedChangePrice(),
                        r.getSignedChangeRate(),
                        r.getAccTradePrice(),
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(r.getUpdatedAt().longValue()), ZoneOffset.UTC)
                        ))
                .map(Ticker::fromDto)
                .toList();

        result.setTickers(tickers);
        result.setExchangeName(exchangeName);

        return result;
    }

    private static MultiValueMap<String,String> createParamMap(String joinedName) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("markets", joinedName);
        return params;
    }
}
