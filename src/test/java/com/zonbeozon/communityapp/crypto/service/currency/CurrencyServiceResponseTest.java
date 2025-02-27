package com.zonbeozon.communityapp.crypto.service.currency;

import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyStatsInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.MiniCurrenciesResponse;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.currency.CurrencyStatsInfo;
import com.zonbeozon.communityapp.crpyto.domain.currency.repository.CurrencyRepository;
import com.zonbeozon.communityapp.crpyto.fetch.currency.CurrencyMetaDataFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.CurrencyQuotesFetcher;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import com.zonbeozon.communityapp.crpyto.service.market.MarketService;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRate;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.exception.FiatException;
import com.zonbeozon.communityapp.exchangerate.service.ExchangeRateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceResponseTest {
    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private Currency currency;

    private static final BigDecimal DUMMY_USD_PRICE = BigDecimal.ONE;
    private static final BigDecimal DUMMY_KRW_PRICE = BigDecimal.TEN;

    public static final String WRONG_FIAT_TYPE = "12";

    @BeforeEach
    void setup() {
        lenient().when(currency.getSymbol()).thenReturn("BTC");
        lenient().when(currency.getCurrencyRank()).thenReturn(1L);
        lenient().when(currency.getWebsite()).thenReturn("dummyWebsite");
        lenient().when(currency.getLogo()).thenReturn("dummyLogo");
        lenient().when(currency.getEnglishName()).thenReturn("bitcoin");
        lenient().when(currency.getId()).thenReturn(1L);
        lenient().when(currency.getKoreanName()).thenReturn("비트코인");
        lenient().when(currency.getKoreanDescription()).thenReturn("더미설명");
        lenient().when(currency.getEnglishDescription()).thenReturn("dummyDescription");
        lenient().when(currency.getCurrencyStatsInfoUsd()).thenReturn(
                CurrencyStatsInfo.builder()
                        .volume(DUMMY_USD_PRICE)
                        .marketCap(DUMMY_USD_PRICE)
                        .totalSupply(DUMMY_USD_PRICE)
                        .fullyDilutedMarketCap(DUMMY_USD_PRICE)
                        .circulatingSupply(DUMMY_USD_PRICE)
                        .build());
        lenient().when(currency.getCurrencyStatsInfoKrw()).thenReturn(
                CurrencyStatsInfo.builder()
                        .volume(DUMMY_KRW_PRICE)
                        .marketCap(DUMMY_KRW_PRICE)
                        .totalSupply(DUMMY_KRW_PRICE)
                        .fullyDilutedMarketCap(DUMMY_KRW_PRICE)
                        .circulatingSupply(DUMMY_KRW_PRICE)
                        .build());

    }

    @Test
    @DisplayName("단일 커런시 조회시 fiatType에 따라 정상적인 CurrencyResponse가 반환되어야 한다")
    void getCurrencyResponse_WithValidFiatType_ShouldReturnCorrectCurrencyResponse() {
        when(currencyRepository.findById(eq(1L))).thenReturn(Optional.of(currency));
        CurrencyResponse currencyResponse = currencyService.getCurrencyResponse(1L, "USD");
        Assertions.assertEquals("BTC", currencyResponse.symbol());
        Assertions.assertEquals("bitcoin", currencyResponse.englishName());
        Assertions.assertEquals("dummyLogo", currencyResponse.logo());
        Assertions.assertEquals("dummyDescription", currencyResponse.englishDescription());
        Assertions.assertEquals("더미설명", currencyResponse.koreanDescription());
        Assertions.assertEquals("비트코인", currencyResponse.koreanName());
        Assertions.assertEquals("dummyWebsite", currencyResponse.website());
        Assertions.assertEquals(1L, currencyResponse.rank());
        Assertions.assertEquals(1L, currencyResponse.id());

        assertEqualsCurrencyResponse(DUMMY_USD_PRICE, currencyResponse.currencyStatsInfo());
    }

    private void assertEqualsCurrencyResponse(BigDecimal expected, CurrencyStatsInfoResponse actual) {
        Assertions.assertEquals(expected, actual.circulatingSupply());
        Assertions.assertEquals(expected, actual.marketCap());
        Assertions.assertEquals(expected, actual.totalSupply());
        Assertions.assertEquals(expected, actual.fullyDilutedMarketCap());
        Assertions.assertEquals(expected, actual.volume());
    }

    @Test
    @DisplayName("전체 커런시 조회시 fiatType에 따라 정상적인 MiniCurrenciesResponse가 반환되어야 한다")
    void getAllMiniCurrenciesResponse_WithValidFiatType_ShouldReturnCorrectResponse() {
        when(currencyRepository.findAll()).thenReturn(List.of(currency));
        MiniCurrenciesResponse miniCurrenciesResponse = currencyService.getAllMiniCurrenciesResponse("KRW");
        Assertions.assertEquals(1, miniCurrenciesResponse.size());
        MiniCurrenciesResponse.SingleMiniCurrencyResponse singleMiniCurrencyResponse = miniCurrenciesResponse.currencies().getFirst();
        Assertions.assertEquals("BTC", singleMiniCurrencyResponse.symbol());
        Assertions.assertEquals("bitcoin", singleMiniCurrencyResponse.englishName());
        Assertions.assertEquals("dummyLogo", singleMiniCurrencyResponse.logo());
        Assertions.assertEquals("비트코인", singleMiniCurrencyResponse.koreanName());
        Assertions.assertEquals(1L, singleMiniCurrencyResponse.rank());
        Assertions.assertEquals(1L, singleMiniCurrencyResponse.id());
        assertEqualsCurrencyResponse(DUMMY_KRW_PRICE, singleMiniCurrencyResponse.currencyStatsInfo());
    }
    @Test
    @DisplayName("커런시 조회시 올바르지 않은 fiatType이 입력되면 예외를 발생시킨다.")
    void getAllMiniCurrenciesResponse_ShouldThrowException_WhenFiatTypeIsInvalid() {
        FiatException fiatException_1 = Assertions.assertThrows(FiatException.class, () -> currencyService.getAllMiniCurrenciesResponse(WRONG_FIAT_TYPE));
        FiatException fiatException_2 = Assertions.assertThrows(FiatException.class, () -> currencyService.getCurrencyResponse(1L, WRONG_FIAT_TYPE));
        Assertions.assertEquals(ErrorCode.FIAT_TYPE_NOT_FOUND, fiatException_1.getErrorCode());
        Assertions.assertEquals(ErrorCode.FIAT_TYPE_NOT_FOUND, fiatException_2.getErrorCode());
    }
}
