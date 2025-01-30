package com.zonbeozon.communityapp.crpyto.fetch.bithumb;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerRequest;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.TickerFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.bithumb.dto.BithumbTickerRequest;
import com.zonbeozon.communityapp.crpyto.fetch.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.fetch.upbit.dto.UpbitTickerRequest;
import com.zonbeozon.communityapp.crpyto.service.ExchangeMarketService;
import com.zonbeozon.communityapp.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BithumbTickerFetcher implements TickerFetcher {
    private static final String BITHUMB_TICKER_API_URL = "https://api.bithumb.com/v1/ticker";
    private static final String exchangeName = "bithumb";

    private final ExchangeMarketService exchangeMarketService;
    private final DefaultFetcher defaultFetcher;

    @Override
    public TickerFetchResult fetch() {
        List<String> marketCodes = exchangeMarketService.getMarketsByExchangeName(exchangeName).stream()
                .map(Market::getMarketCode)
                .toList();
        if(marketCodes.isEmpty()) throw new ExchangeException(ErrorCode.EMPTY_MARKET_EXCHANGE);
        String joinedName = String.join(",", marketCodes);
        List<BithumbTickerRequest> tickerRequest = defaultFetcher.fetchWithParam(
                BITHUMB_TICKER_API_URL,
                createParamMap(joinedName),
                new ParameterizedTypeReference<>() {});
        return convert(tickerRequest);
    }

    public TickerFetchResult convert(List<BithumbTickerRequest> bithumbTickerRequests) {
        TickerFetchResult result = new TickerFetchResult();

        List<Ticker> tickers = bithumbTickerRequests.stream()
                .map(r-> TickerRequest.builder()
                        .marketCode(r.getMarketCode())
                        .openingPrice(r.getOpeningPrice())
                        .highPrice(r.getHighPrice())
                        .lowPrice(r.getLowPrice())
                        .tradePrice(r.getTradePrice())
                        .signedChangePrice(r.getSignedChangePrice())
                        .signedChangeRate(r.getSignedChangeRate())
                        .accTradePrice(r.getAccTradePrice())
                        .build())
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
