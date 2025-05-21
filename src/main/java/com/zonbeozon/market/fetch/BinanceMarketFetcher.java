package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.fiat.entity.FiatType;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;

@Component
class BinanceMarketFetcher
        extends AbstractMarketFetcher<BinanceMarketFetchResponse>
        implements MarketFetcher
{
    private static final String BINANCE_BASE_URL = "api.binance.com";
    private static final String BINANCE_MARKET_RESOURCE_URL = "/api/v3/ticker/tradingDay";

    public BinanceMarketFetcher(RestClient.Builder restClientBuilder) {
        super(restClientBuilder, new ParameterizedTypeReference<>() {});
    }

    @Override
    protected Exchange getExchange() {
        return Exchange.BINANCE;
    }

    @Override
    protected FiatType getFiatType() {
        return FiatType.USD;
    }

    @Override
    protected String getBaseUrl() {
        return BINANCE_BASE_URL;
    }

    @Override
    protected String getResourceUrl() {
        return BINANCE_MARKET_RESOURCE_URL;
    }

    @Override
    protected MultiValueMap<String, String> getMarketCodeParams(Set<String> marketCodes) {
        String joinedName = "[\"" + String.join("\",\"", marketCodes) + "\"]";
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("symbols", joinedName);
        return params;
    }
}
