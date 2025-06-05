package com.zonbeozon.market.fetch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

public class MockMarketServer {
    private final MockRestServiceServer server;
    private final ObjectMapper objectMapper;

    public MockMarketServer(
            RestClient.Builder restClientBuilder,
            String expectedUri,
            String expectedParamKey,
            String expectedParamValue,
            List<MarketFetchResponse> expectedResponse
    ) {
        this.server = MockRestServiceServer.bindTo(restClientBuilder).build();
        this.objectMapper = new ObjectMapper();

        server.expect(requestTo(startsWith(expectedUri)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam(expectedParamKey, UriUtils.encodeQueryParam(expectedParamValue, StandardCharsets.UTF_8)))
                .andRespond(MockRestResponseCreators.withSuccess(convertResponseToString(expectedResponse), MediaType.APPLICATION_JSON));

    }

    private String convertResponseToString(List<MarketFetchResponse> response) {
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
