package com.zonbeozon.communityapp.crpyto.fetch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DefaultFetcher {
    private final RestTemplate restTemplate;
    private final Validator validator;

    public <T> T fetchWithParam(
            String url,
            MultiValueMap<String, String> paramMap,
            ParameterizedTypeReference<T> responseType
    ) {
        String paramIncludedUrl = UriComponentsBuilder.fromUriString(url)
                .queryParams(paramMap).build().toUriString();
        return fetchDto(paramIncludedUrl, responseType);
    }

    private <T> T fetchDto(String url, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    responseType
                    );
            T dto = responseEntity.getBody();
            if(dto == null) {
                throw new RuntimeException("DTO is null");
            }
            validate(dto);
            return dto;
        } catch (RestClientException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    private void validate(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Validation failed for DTO", violations);
        }
    }
}
