package com.zonbeozon.currency.fetch;

import com.zonbeozon.global.fetch.FetchContextSupplier;
import com.zonbeozon.global.fetch.FetchManager;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
class CMCMetadataFetcher extends CMCAbstractFetcher implements FetchManager<CurrencyFetchContext, CurrencyMetadataFetchResult> {
    private final static String METADATA_RESOURCE_URL = "/v1/cryptocurrency/info";
    private final static String METADATA_AUX = "urls,logo,description";

    @Autowired
    public CMCMetadataFetcher(
            @Value("${cmc.key}") String key,
            RestClient.Builder restClientBuilder,
            Validator validator
    ) {
        super(key, restClientBuilder, validator);
    }

    @Override
    public CurrencyMetadataFetchResult fetch(FetchContextSupplier<CurrencyFetchContext> contextSupplier) {
        return super.basicFetch(contextSupplier.getContext().getSymbols(), CMCMetadataResponse.class).toResult();
    }
    @Override
    protected String getPath() {
        return METADATA_RESOURCE_URL;
    }

    @Override
    protected String getAux() {
        return METADATA_AUX;
    }

    @Override
    public String toString() {
        return "CMCCurrencyMetadataFetcher" + this.hashCode();
    }
}
