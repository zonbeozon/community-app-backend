package com.zonbeozon.fiat.fetch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.global.utils.BigDecimalUtils;
import com.zonbeozon.fiat.entity.ConversionRateCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

//public class NaverUsdToKrwConversionRateFetcherTest {
//    private final RestClient.Builder restClientBuilder = RestClient.builder();
//    private final MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final NaverUsdToKrwConversionRateFetcher naverUsdToKrwConversionRateFetcher = new NaverUsdToKrwConversionRateFetcher(restClientBuilder);
//
//    @BeforeEach
//    void setup() {
//        server.reset();
//    }
//
//    @Test
//    @DisplayName("환율 정보를 정상적으로 가져와야 한다.")
//    void shouldFetchConversionRateSuccessfully() throws JsonProcessingException {
//        String value = "1,417.50";
//        BigDecimal valueToBigDecimal = BigDecimalUtils.StringToBigDecimal(value.replace(",", ""), BigDecimalUtils.FIAT_SCALE);
//
//        String mockResponseJson = createJsonMockResponse(value);
//
//        server.expect(requestTo("https://search.naver.com/p/csearch/content/qapirender.nhn?key=calculator&pkid=141&q=%25ED%2599%2598%25EC%259C%25A8&where=m&u1=keb&u6=standardUnit&u7=0&u3=USD&u4=KRW&u8=down&u2=1"))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(MockRestResponseCreators.withSuccess(mockResponseJson, MediaType.APPLICATION_JSON));
//
//        ConversionRateFetchResult result = naverUsdToKrwConversionRateFetcher.fetch(ConversionRateCode.USD_KRW);
//        Assertions.assertThat(result)
//                .isNotNull()
//                .returns(ConversionRateCode.USD_KRW, ConversionRateFetchResult::conversionRateCode)
//                .returns(valueToBigDecimal, ConversionRateFetchResult::value);
//    }
//
//    private String createJsonMockResponse(String value) throws JsonProcessingException {
//        // CountryInfo 객체 생성 (mock 데이터)
//        NaverConversionRateResponse.CountryInfo countryInfo1 = new NaverConversionRateResponse.CountryInfo(
//                "1", "1 달러", "달러"
//        );
//        NaverConversionRateResponse.CountryInfo countryInfo2 = new NaverConversionRateResponse.CountryInfo(
//                value, "1,417.50 원", "원"
//        );
//        // NaverConversionRateResponse 객체 생성 (mock 데이터)
//        NaverConversionRateResponse mockResponse = new NaverConversionRateResponse(
//                141L, 1, List.of(countryInfo1, countryInfo2), "환율 계산기 메시지"
//        );
//        return objectMapper.writeValueAsString(mockResponse);
//    }
//}
