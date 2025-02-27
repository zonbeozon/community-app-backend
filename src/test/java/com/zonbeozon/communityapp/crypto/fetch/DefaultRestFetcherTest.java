package com.zonbeozon.communityapp.crypto.fetch;

import com.zonbeozon.communityapp.crpyto.exception.FetchException;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultRestFetcherTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Validator validator;
    @Mock
    private ResponseEntity<Object> responseEntity;
    @InjectMocks
    private DefaultRestFetcher defaultRestFetcher;

    String exampleUrl = "http://example.com";
    String exampleParamValue = "key";
    String exampleParamName = "value";

    String paramIncludeUrl = createUrl();
    MultiValueMap<String, String> params = createParams();
    ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<>() {};

    @Test
    @DisplayName("요청한 url 파라미터 응답 타입에 맞게 요청을 보내야 한다")
    void shouldSendGetRequestWithCorrectUrlAndResponseType() {
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        when(responseEntity.getBody()).thenReturn(new Object());
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
        when(validator.validate(any())).thenReturn(Set.of());

        defaultRestFetcher.fetchWithParam(exampleUrl, params, responseType);

        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.GET),
                eq(null),
                eq(responseType));

        Assertions.assertEquals(paramIncludeUrl, urlCaptor.getValue());
    }

    @Test
    @DisplayName("검증에서 문제가 발생하면 예외를 발생시킨다")
    void shouldThrowConstraintViolationExceptionWhenValidationFails() {
        when(responseEntity.getBody()).thenReturn(responseEntity);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(responseEntity);
        when(validator.validate(any())).thenReturn(Set.of(createConstraintViolation()));

        Assertions.assertThrows(FetchException.class,
                () -> defaultRestFetcher.fetchWithParam(exampleUrl, params, responseType));
    }

    @Test
    @DisplayName("restTemplate_예외가_발생하면_예외를_발생시킨다")
    void shouldThrowFetchExceptionWhenRestTemplateThrowsException() {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);

        Assertions.assertThrows(FetchException.class,
                () -> defaultRestFetcher.fetchWithParam(exampleUrl, params, responseType));
    }

    private MultiValueMap<String, String> createParams() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(exampleParamName, exampleParamValue);
        return multiValueMap;
    }

    private String createUrl() {
        return exampleUrl + "?" + exampleParamName + "=" + exampleParamValue;
    }

    private ConstraintViolation<Object> createConstraintViolation() {
        return mock(ConstraintViolation.class);
    }
}
