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
class UpbitMarketFetcher
        extends AbstractMarketFetcher<UpbitMarketFetchResponse>
        implements MarketFetcher
{
    private static final String UPBIT_BASE_URL = "api.upbit.com";
    private static final String UPBIT_MARKET_RESOURCE_URL = "/v1/ticker";

    public UpbitMarketFetcher(RestClient.Builder restClientBuilder) {
        super(restClientBuilder, new ParameterizedTypeReference<>() {
        });
    }

    @Override
    protected Exchange getExchange() {
        return Exchange.UPBIT;
    }

    @Override
    protected FiatType getFiatType() {
        return FiatType.KRW;
    }

    @Override
    protected String getBaseUrl() {
        return UPBIT_BASE_URL;
    }

    @Override
    protected String getResourceUrl() {
        return UPBIT_MARKET_RESOURCE_URL;
    }

    @Override
    protected MultiValueMap<String, String> getMarketCodeParams(Set<String> marketCodes) {
        String joinedName = String.join(",", marketCodes);
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("markets", joinedName);
        return params;
    }
}
