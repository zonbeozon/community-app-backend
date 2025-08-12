package com.zonbeozon.crypto.fetcher;

import com.zonbeozon.crypto.enums.LanguageCode;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class CMCMetadataFetcher extends CMCAbstractFetcher implements MetadataFetcher {
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
    public MetadataFetchResult fetch(Collection<String> symbols) {
        CMCMetadataResponse response = super.fetch(symbols, CMCMetadataResponse.class);
        Set<CurrencyMetaData> metaDataSet = response.metadataMap().values().stream()
                .map(metadata -> new CurrencyMetaData(
                        metadata.symbol(),
                        metadata.name(),
                        metadata.description(),
                        metadata.logo(),
                        metadata.urls().websites().getFirst()))
                .collect(Collectors.toSet());

        return new MetadataFetchResult(LanguageCode.EN, metaDataSet);
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
