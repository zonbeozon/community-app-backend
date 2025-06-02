package com.zonbeozon.market.fetch;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.fiat.entity.FiatType;
import com.zonbeozon.market.MarketHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;

@Component
class BithumbMarketFetcher
        extends AbstractMarketFetcher<BithumbMarketFetchResponse>
        implements MarketFetcher
{
    private static final String BITHUMB_BASE_URL = "api.bithumb.com";
    private static final String BITHUMB_MARKET_RESOURCE_URL = "/v1/ticker";

    public BithumbMarketFetcher(RestClient.Builder restClientBuilder) {
        super(restClientBuilder, new ParameterizedTypeReference<>() {
        });
    }

    @Override
    protected Exchange getExchange() {
        return Exchange.BITHUMB;
    }

    @Override
    protected FiatType getFiatType() {
        return FiatType.KRW;
    }


    @Override
    protected String getBaseUrl() {
        return BITHUMB_BASE_URL;
    }

    @Override
    protected String getResourceUrl() {
        return BITHUMB_MARKET_RESOURCE_URL;
    }

    @Override
    protected MultiValueMap<String, String> convertMarketToParam(MarketHolder marketHolder) {
        String joinedName = String.join(",", marketHolder.toMarketCodeSet());
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("markets", joinedName);
        return params;
    }
}