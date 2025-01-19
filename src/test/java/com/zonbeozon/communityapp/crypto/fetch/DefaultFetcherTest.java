package com.zonbeozon.communityapp.crypto.fetch;

import com.zonbeozon.communityapp.crpyto.fetch.DefaultFetcher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static org.mockito.Mockito.*;

public class DefaultFetcherTest {
    private DefaultFetcher defaultFetcher;
    private RestTemplate mockRestTemplate;
    private Validator mockValidator;

    String exampleUrl = "http://example.com";
    String exampleParamValue = "key";
    String exampleParamName = "value";

    String paramIncludeUrl = createUrl();
    MultiValueMap<String, String> params = createParams();
    ParameterizedTypeReference<TestClass> responseType = new ParameterizedTypeReference<>() {};


    @BeforeEach
    void setup() {
        mockRestTemplate = mock(RestTemplate.class);
        mockValidator = mock(Validator.class);
        defaultFetcher = new DefaultFetcher(mockRestTemplate, mockValidator);
    }

    @Test
    void 요청한_url_파라미터_응답_타입에_맞게_요청을_보내야_한다() {
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        ResponseEntity<TestClass> mockResponse = mock(ResponseEntity.class);
        when(mockResponse.getBody()).thenReturn(new TestClass());
        when(mockRestTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(mockResponse);
        when(mockValidator.validate(any())).thenReturn(Set.of());

        defaultFetcher.fetchWithParam(exampleUrl, params, responseType);

        verify(mockRestTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.GET),
                eq(null),
                eq(responseType));

        Assertions.assertEquals(paramIncludeUrl, urlCaptor.getValue());
    }

    @Test
    void 검증에서_문제가_발생하면_예외를_발생시킨다() {
        ResponseEntity<TestClass> mockResponse = mock(ResponseEntity.class);
        when(mockResponse.getBody()).thenReturn(new TestClass());
        when(mockRestTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenReturn(mockResponse);
        when(mockValidator.validate(any())).thenReturn(Set.of(createConstraintViolation()));

        Assertions.assertThrows(ConstraintViolationException.class,
                () -> defaultFetcher.fetchWithParam(exampleUrl, params, responseType));
    }

    @Test
    void restTemplate_에러가_발생하면_예외를_발생시킨다() {
        when(mockRestTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(null),
                any(ParameterizedTypeReference.class))).thenThrow(RestClientException.class);

        Assertions.assertThrows(RuntimeException.class,
                () -> defaultFetcher.fetchWithParam(exampleUrl, params, responseType));
    }

    private MultiValueMap<String, String> createParams() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(exampleParamName, exampleParamValue);
        return multiValueMap;
    }

    private String createUrl() {
        return exampleUrl + "?" + exampleParamName + "=" + exampleParamValue;
    }

    private static class TestClass {}

    private ConstraintViolation<Object> createConstraintViolation() {
        return mock(ConstraintViolation.class);
    }
}
