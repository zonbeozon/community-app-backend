package com.zonbeozon.currency.fetch;

import com.zonbeozon.global.fetch.FetchContextSupplier;
import com.zonbeozon.global.fetch.FetchManager;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class CMCQuotesFetcher extends CMCAbstractFetcher implements FetchManager<CurrencyFetchContext, CurrencyQuotesFetchResult> {
    private static final String QUOTES_RESOURCE_URL = "/v1/cryptocurrency/quotes/latest";
    private static final String QUOTES_AUX = "cmc_rank,circulating_supply,max_supply,total_supply";

    @Autowired
    public CMCQuotesFetcher(
            final @Value("${cmc.key}") String key,
            final RestClient.Builder restClientBuilder,
            final Validator validator
    ) {
        super(key, restClientBuilder, validator);
    }

    @Override
    public CurrencyQuotesFetchResult fetch(FetchContextSupplier<CurrencyFetchContext> contextSupplier) {
        return super.basicFetch(contextSupplier.getContext().getSymbols(), CMCQuotesResponse.class).toResult();
    }

    @Override
    protected String getPath() {
        return QUOTES_RESOURCE_URL;
    }

    @Override
    protected String getAux() {
        return QUOTES_AUX;
    }

    @Override
    public String toString() {
        return "CMCCurrencyQuotesFetcher" + this.hashCode();
    }
}
