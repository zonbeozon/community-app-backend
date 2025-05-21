package com.zonbeozon.currency.fetch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.zonbeozon.currency.test.CommonCurrencyRelatedData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CMCMetadataFetcherTest {
    private static final String TEST_KEY = "1234";
    private static final RestClient.Builder restClientBuilder = RestClient.builder();
    private static final MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private Validator validator;
    private CMCMetadataFetcher cmcMetadataFetcher;

    @BeforeEach
    void setup() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        cmcMetadataFetcher = new CMCMetadataFetcher(TEST_KEY, restClientBuilder, validator);
        server.reset();
    }

    @Test
    @DisplayName("metadata를 정상적으로 가져와야 한다.")
    void givenCryptoSymbols_whenFetchMetadata_thenReturnsValidMetadata() throws JsonProcessingException {
        server.expect(requestTo("https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?symbol=BTC,ETH&aux=urls,logo,description"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-CMC_PRO_API_KEY", TEST_KEY))
                .andRespond(MockRestResponseCreators.withSuccess(createDummyJsonResponse(), MediaType.APPLICATION_JSON));

        CMCMetadataResponse response = cmcMetadataFetcher.fetch(List.of(BTC_SYMBOL, ETH_SYMBOL));

        Assertions.assertThat(response.metadataMap())
                .containsOnlyKeys(BTC_SYMBOL, ETH_SYMBOL);

        Assertions.assertThat(response.metadataMap().get(BTC_SYMBOL))
                .extracting(
                        CMCMetadataResponse.CMCMetadata::englishName,
                        CMCMetadataResponse.CMCMetadata::symbol,
                        CMCMetadataResponse.CMCMetadata::logo,
                        CMCMetadataResponse.CMCMetadata::englishDescription,
                        metadata -> metadata.urls().website()
                )
                .containsExactly(
                        BTC_EN_NAME,
                        BTC_SYMBOL,
                        BTC_LOGO,
                        BTC_EN_DESCRIPTION,
                        List.of(BTC_WEBSITE)
                );

        Assertions.assertThat(response.metadataMap().get(ETH_SYMBOL))
                .extracting(
                        CMCMetadataResponse.CMCMetadata::englishName,
                        CMCMetadataResponse.CMCMetadata::symbol,
                        CMCMetadataResponse.CMCMetadata::logo,
                        CMCMetadataResponse.CMCMetadata::englishDescription,
                        metadata -> metadata.urls().website()
                )
                .containsExactly(
                        ETH_EN_NAME,
                        ETH_SYMBOL,
                        ETH_LOGO,
                        ETH_EN_DESCRIPTION,
                        List.of(ETH_WEBSITE)
                );
    }

    private String createDummyJsonResponse() throws JsonProcessingException {
        CMCMetadataResponse response =  new CMCMetadataResponse(
                Map.of(
                        BTC_SYMBOL, new CMCMetadataResponse.CMCMetadata(
                                BTC_EN_NAME,
                                BTC_SYMBOL,
                                BTC_LOGO,
                                BTC_EN_DESCRIPTION,
                                new CMCMetadataResponse.CMCUrls(List.of(BTC_WEBSITE))
                        ),
                        ETH_SYMBOL, new CMCMetadataResponse.CMCMetadata(
                                ETH_EN_NAME,
                                ETH_SYMBOL,
                                ETH_LOGO,
                                ETH_EN_DESCRIPTION,
                                new CMCMetadataResponse.CMCUrls(List.of(ETH_WEBSITE))
                        )
                )
        );
        return objectMapper.writeValueAsString(response);
    }
}
