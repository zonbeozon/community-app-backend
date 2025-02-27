package com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap;

import com.nimbusds.oauth2.sdk.util.MapUtils;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.CurrencyMetaDataFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap.dto.CMCCurrencyMetaDataRequest;
import com.zonbeozon.communityapp.crpyto.domain.currency.dto.CurrencyMetaData;
import com.zonbeozon.communityapp.crpyto.fetch.currency.dto.CurrencyMetaDataFetchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap.CMCUrls.*;

@Component
public class CMCCurrencyMetaDataFetcher implements CurrencyMetaDataFetcher {
    private final DefaultRestFetcher restFetcher;
    private final String key;

    @Autowired
    public CMCCurrencyMetaDataFetcher(
            DefaultRestFetcher restFetcher,
            @Value("${cmc.key}") String key
    ) {
        this.restFetcher = restFetcher;
        this.key = key;
    }

    @Override
    public CurrencyMetaDataFetchResult fetch(Iterable<String> symbols) {
        String joinedSymbol = String.join(",", symbols);
        CMCCurrencyMetaDataRequest CMCCurrencyMetaDataRequest = restFetcher.fetchWithParam(
                BASE_URL + METADATA_RESOURCE_URL,
                createParamMap(joinedSymbol, METADATA_AUX),
                new ParameterizedTypeReference<>() {},
                createHeaderMap());

        return convert(CMCCurrencyMetaDataRequest);
    }

    private Map<String, String> createHeaderMap() {
        return Map.of("X-CMC_PRO_API_KEY", key);
    }

    private CurrencyMetaDataFetchResult convert(CMCCurrencyMetaDataRequest cmcCurrencyMetaDataRequest) {
        List<CurrencyMetaData> convertedCurrencyMetaData = cmcCurrencyMetaDataRequest.currencyMetaDataMap().values().stream()
                .map(v -> CurrencyMetaData.builder()
                            .englishName(v.englishName())
                            .logo(v.logo())
                            .symbol(v.symbol())
                            .englishDescription(v.englishDescription())
                            .url(v.urls().website().getFirst())
                            .build()
                )
                .toList();

        return new CurrencyMetaDataFetchResult(convertedCurrencyMetaData);
    }

    private static MultiValueMap<String,String> createParamMap(String joinedSymbol, String aux) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("symbol", joinedSymbol);
        params.add("aux", aux);
        return params;
    }
}
