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
public class CMCQuotesFetcherTest {
    private static final String TEST_KEY = "1234";
    private static final RestClient.Builder restClientBuilder = RestClient.builder();
    private static final MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private Validator validator;
    private CMCQuotesFetcher cmcQuotesFetcher;

    @BeforeEach
    void setup() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        cmcQuotesFetcher = new CMCQuotesFetcher(TEST_KEY, restClientBuilder, validator);
        server.reset();
    }

    @Test
    @DisplayName("quotes를 정상적으로 가져와야 한다.")
    void givenCryptoSymbols_whenFetchQuotes_thenReturnsValidQuotes() throws JsonProcessingException {
        server.expect(requestTo("https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol=BTC,ETH&aux=cmc_rank,circulating_supply,max_supply,total_supply"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-CMC_PRO_API_KEY", TEST_KEY))
                .andRespond(MockRestResponseCreators.withSuccess(createDummyJsonResponse(), MediaType.APPLICATION_JSON));

        CMCQuotesResponse response = cmcQuotesFetcher.fetch(List.of(BTC_SYMBOL, ETH_SYMBOL));

        Assertions.assertThat(response.quoteMap())
                .containsOnlyKeys(BTC_SYMBOL, ETH_SYMBOL);

        Assertions.assertThat(response.quoteMap().get(BTC_SYMBOL))
                .extracting(
                        CMCQuotesResponse.CMCQuote::symbol,
                        CMCQuotesResponse.CMCQuote::rank,
                        CMCQuotesResponse.CMCQuote::circulatingSupply,
                        CMCQuotesResponse.CMCQuote::totalSupply,
                        cmcQuote -> cmcQuote.UsdDetails().quoteUsdPriceDetails().fullyDilutedMarketCap(),
                        cmcQuote -> cmcQuote.UsdDetails().quoteUsdPriceDetails().marketCap(),
                        cmcQuote -> cmcQuote.UsdDetails().quoteUsdPriceDetails().volume()
                )
                .containsExactly(
                        BTC_SYMBOL,
                        BTC_RANK,
                        BTC_CIRCULATING_SUPPLY,
                        BTC_TOTAL_SUPPLY,
                        BTC_USD_FULLY_DILUTED_MARKET_CAP,
                        BTC_USD_MARKET_CAP,
                        BTC_USD_VOLUME_24H
                );

        Assertions.assertThat(response.quoteMap().get(ETH_SYMBOL))
                .extracting(
                        CMCQuotesResponse.CMCQuote::symbol,
                        CMCQuotesResponse.CMCQuote::rank,
                        CMCQuotesResponse.CMCQuote::circulatingSupply,
                        CMCQuotesResponse.CMCQuote::totalSupply,
                        cmcQuote -> cmcQuote.UsdDetails().quoteUsdPriceDetails().fullyDilutedMarketCap(),
                        cmcQuote -> cmcQuote.UsdDetails().quoteUsdPriceDetails().marketCap(),
                        cmcQuote -> cmcQuote.UsdDetails().quoteUsdPriceDetails().volume()
                )
                .containsExactly(
                        ETH_SYMBOL,
                        ETH_RANK,
                        ETH_CIRCULATING_SUPPLY,
                        ETH_TOTAL_SUPPLY,
                        ETH_USD_FULLY_DILUTED_MARKET_CAP,
                        ETH_USD_MARKET_CAP,
                        ETH_USD_VOLUME_24H
                );
    }

    private String createDummyJsonResponse() throws JsonProcessingException {
        CMCQuotesResponse response = new CMCQuotesResponse(
                Map.of(
                        BTC_SYMBOL, new CMCQuotesResponse.CMCQuote(
                                BTC_SYMBOL,
                                BTC_RANK,
                                BTC_CIRCULATING_SUPPLY,
                                BTC_TOTAL_SUPPLY,
                                new CMCQuotesResponse.USDDetails(
                                        new CMCQuotesResponse.QuotePriceDetail(
                                        BTC_USD_VOLUME_24H,
                                        BTC_USD_MARKET_CAP,
                                        BTC_USD_FULLY_DILUTED_MARKET_CAP
                                ))
                        ),
                        ETH_SYMBOL, new CMCQuotesResponse.CMCQuote(
                                ETH_SYMBOL,
                                ETH_RANK,
                                ETH_CIRCULATING_SUPPLY,
                                ETH_TOTAL_SUPPLY,
                                new CMCQuotesResponse.USDDetails(
                                        new CMCQuotesResponse.QuotePriceDetail(
                                                ETH_USD_VOLUME_24H,
                                                ETH_USD_MARKET_CAP,
                                                ETH_USD_FULLY_DILUTED_MARKET_CAP
                                        ))
                        )
                )
        );
        return objectMapper.writeValueAsString(response);
    }
}
