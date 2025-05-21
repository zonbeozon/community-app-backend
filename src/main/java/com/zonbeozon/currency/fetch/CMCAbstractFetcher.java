package com.zonbeozon.currency.fetch;

import com.zonbeozon.common.fetch.FetchException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

abstract class CMCAbstractFetcher {
    private static final String BASE_URL = "pro-api.coinmarketcap.com";
    private static final String SYMBOL_PARAM_KEY = "symbol";
    private static final String AUX_PARAM_KEY = "aux";
    private static final String AUTH_HEADER_KEY = "X-CMC_PRO_API_KEY";

    private final String key;
    private final RestClient restClient;
    private final Validator validator;

    protected CMCAbstractFetcher(
            String key,
            RestClient.Builder restClientBuilder,
            Validator validator
    ) {
        this.restClient = restClientBuilder.build();
        this.key = key;
        this.validator = validator;
    }

    protected <T> T basicFetch(Collection<String> symbols, Class<T> clazz) {
        String joinedSymbol = String.join(",", symbols);
        try {
            T response = Optional.ofNullable(restClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .scheme("https")
                                    .host(BASE_URL)
                                    .path(getPath())
                                    .queryParam(SYMBOL_PARAM_KEY, joinedSymbol)
                                    .queryParam(AUX_PARAM_KEY, getAux())
                                    .build())
                            .header(AUTH_HEADER_KEY, key)
                            .retrieve()
                            .body(clazz))
                    .orElseThrow(() -> new FetchException("response body is null"));
            Set<ConstraintViolation<Object>> violations = validator.validate(response);
            if (!violations.isEmpty())
                throw new FetchException(violations.toString());
            return response;
        } catch (RestClientException e) {
            throw new FetchException(e);
        }
    }
    protected abstract String getPath();
    protected abstract String getAux();
}
