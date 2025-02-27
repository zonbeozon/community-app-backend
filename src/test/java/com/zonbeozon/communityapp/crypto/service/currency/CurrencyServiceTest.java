package com.zonbeozon.communityapp.crypto.service.currency;

import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyRequest;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketAddRequest;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketRequest;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.currency.CurrencyStatsInfo;
import com.zonbeozon.communityapp.crpyto.domain.currency.dto.CurrencyMetaData;
import com.zonbeozon.communityapp.crpyto.domain.currency.dto.CurrencyQuote;
import com.zonbeozon.communityapp.crpyto.domain.currency.repository.CurrencyRepository;
import com.zonbeozon.communityapp.crpyto.exception.CurrencyException;
import com.zonbeozon.communityapp.crpyto.fetch.currency.CurrencyMetaDataFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.CurrencyQuotesFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.dto.CurrencyMetaDataFetchResult;
import com.zonbeozon.communityapp.crpyto.fetch.currency.dto.CurrencyQuotesFetchResult;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import com.zonbeozon.communityapp.crpyto.service.market.MarketService;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRate;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.service.ExchangeRateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {
    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private CurrencyMetaDataFetcher currencyMetaDataFetcher;
    @Mock
    private CurrencyQuotesFetcher currencyQuotesFetcher;
    @Mock
    private MarketService marketService;
    @Mock
    private ExchangeRateService exchangeRateService;
    @Mock
    private MarketRequest marketRequest;

    private final CurrencyQuote currencyQuote = CurrencyQuote.builder()
            .rank(1L)
            .symbol("BTC")
            .fullyDilutedMarketCap(BigDecimal.ONE)
            .marketCap(BigDecimal.ONE)
            .circulatingSupply(BigDecimal.ONE)
            .totalSupply(BigDecimal.ONE)
            .volume(BigDecimal.ONE)
            .build();

    private final CurrencyMetaData currencyMetaData = CurrencyMetaData.builder()
            .symbol("BTC")
            .englishDescription("")
            .englishName("bitcoin")
            .url("https://bitcoin.org")
            .logo("https://exampleLogo.com")
            .build();

    @Mock
    private CurrencyRequest currencyRequest;
    @Mock
    private ExchangeRate exchangeRate;
    @Mock
    private Currency currency;
    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    void setup() {
        lenient().when(currencyRequest.symbol()).thenReturn("BTC");
        lenient().when(currencyRequest.markets()).thenReturn(Collections.singletonList(marketRequest));
        lenient().when(currencyQuotesFetcher.fetch(anyIterable()))
                .thenReturn(new CurrencyQuotesFetchResult(Collections.singletonList(currencyQuote)));
        lenient().when(currencyMetaDataFetcher.fetch(anyIterable()))
                .thenReturn(new CurrencyMetaDataFetchResult(Collections.singletonList(currencyMetaData)));
        lenient().when(exchangeRateService.getLatestExchangeRate(ExchangeRateCode.USD))
                .thenReturn(exchangeRate);
        lenient().when(exchangeRate.getRate()).thenReturn(BigDecimal.TEN);
        lenient().when(currency.getSymbol()).thenReturn("BTC");
    }

    @Test
    @DisplayName("currency 추가시 fetch가 성공적이면 저장하고 마켓 서비스를 호출한다.")
    void shouldSaveCurrencyAndCallMarketServiceWhenFetchSucceeds() {
        ArgumentCaptor<Currency> captor = ArgumentCaptor.forClass(Currency.class);
        when(currencyRepository.existsBySymbol(anyString())).thenReturn(false);
        when(currencyRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        currencyService.addCurrency(currencyRequest);

        verify(marketService, times(1)).addMarkets(anyCollection(), any(Currency.class));

        Currency capturedCurrency = captor.getValue();
        assertEqualsCurrency(capturedCurrency);
    }

    private void assertEqualsCurrency(Currency actual) {
        Assertions.assertEquals(1L, actual.getCurrencyRank());
        Assertions.assertEquals("BTC", actual.getSymbol());
        Assertions.assertEquals("bitcoin", actual.getEnglishName());
        Assertions.assertEquals("", actual.getEnglishDescription());
        Assertions.assertEquals("", actual.getKoreanDescription());
        Assertions.assertEquals("https://exampleLogo.com", actual.getLogo());
        Assertions.assertEquals("https://bitcoin.org", actual.getWebsite());
        Assertions.assertEquals(BigDecimal.TEN, actual.getCurrencyStatsInfoKrw().getMarketCap());
        Assertions.assertEquals(BigDecimal.TEN, actual.getCurrencyStatsInfoKrw().getVolume());
        Assertions.assertEquals(BigDecimal.TEN, actual.getCurrencyStatsInfoKrw().getFullyDilutedMarketCap());
        Assertions.assertEquals(BigDecimal.TEN, actual.getCurrencyStatsInfoKrw().getCirculatingSupply());
        Assertions.assertEquals(BigDecimal.TEN, actual.getCurrencyStatsInfoKrw().getTotalSupply());
        Assertions.assertEquals(BigDecimal.ONE, actual.getCurrencyStatsInfoUsd().getMarketCap());
        Assertions.assertEquals(BigDecimal.ONE, actual.getCurrencyStatsInfoUsd().getVolume());
        Assertions.assertEquals(BigDecimal.ONE, actual.getCurrencyStatsInfoUsd().getFullyDilutedMarketCap());
        Assertions.assertEquals(BigDecimal.ONE, actual.getCurrencyStatsInfoUsd().getCirculatingSupply());
        Assertions.assertEquals(BigDecimal.ONE, actual.getCurrencyStatsInfoUsd().getTotalSupply());
    }

    @Test
    @DisplayName("중복 symbol이 존재하면 예외를 발생시킨다.")
    void shouldThrowExceptionWhenDuplicateSymbolExists() {
        when(currencyRepository.existsBySymbol(anyString())).thenReturn(true);

        CurrencyException currencyException = Assertions.assertThrows(CurrencyException.class,
                () -> currencyService.addCurrency(currencyRequest));
        Assertions.assertEquals(ErrorCode.DUPLICATE_CURRENCY_SYMBOL, currencyException.getErrorCode());
    }

    @Test
    @DisplayName("단일 마켓 추가시 해당하는 currency를 찾고 마켓 서비스를 호출한다.")
    void shouldFindCurrencyAndCallMarketServiceWhenAddingSingleMarket() {
        MarketAddRequest marketAddRequest = new MarketAddRequest(1L, "USDT", "binance");
        when(currencyRepository.findById(eq(1L))).thenReturn(Optional.of(currency));
        currencyService.addMarket(marketAddRequest);

        verify(marketService, times(1)).addMarkets(anyCollection(), any(Currency.class));
    }

    @Test
    @DisplayName("currency quote 업데이트는 fetch가 성공적이라면 CurrencyQuote필드가 업데이트 되어야한다.")
    void shouldUpdateCurrencyQuoteForAllCurrenciesWhenFetchSucceeds() {
        ArgumentCaptor<CurrencyStatsInfo> captor = ArgumentCaptor.forClass(CurrencyStatsInfo.class);
        when(currencyRepository.findAll()).thenReturn(List.of(currency));
        currencyService.updateCurrencyQuotes();
        verify(currency, times(1)).updateQuote(eq(1L) ,captor.capture(), captor.capture());
        CurrencyStatsInfo capturedCurrencyStatsUsd = captor.getAllValues().get(1);
        CurrencyStatsInfo capturedCurrencyStatsKrw = captor.getAllValues().getFirst();

        assertEqualsCurrencyStatsInfo(BigDecimal.ONE, capturedCurrencyStatsUsd);
        assertEqualsCurrencyStatsInfo(BigDecimal.TEN, capturedCurrencyStatsKrw);
    }

    private void assertEqualsCurrencyStatsInfo(BigDecimal expected, CurrencyStatsInfo actual) {
        Assertions.assertEquals(expected, actual.getMarketCap());
        Assertions.assertEquals(expected, actual.getFullyDilutedMarketCap());
        Assertions.assertEquals(expected, actual.getVolume());
        Assertions.assertEquals(expected, actual.getCirculatingSupply());
        Assertions.assertEquals(expected, actual.getTotalSupply());
    }

    @Test
    @DisplayName("영문 설명 업데이트시 currencyId가 존재한다면 업데이트 되어야한다.")
    void shouldUpdateEnglishDescriptionWhenCurrencyIdExists() {
        when(currencyRepository.findById(eq(1L))).thenReturn(Optional.of(currency));
        currencyService.updateEnglishDescription(1L, "dummy");
        verify(currency, times(1)).updateEnglishDescription(eq("dummy"));
    }

    @Test
    @DisplayName("한글 설명 업데이트시 currencyId가 존재한다면 업데이트 되어야한다.")
    void shouldUpdateKoreanDescriptionWhenCurrencyIdExists() {
        when(currencyRepository.findById(eq(1L))).thenReturn(Optional.of(currency));
        currencyService.updateKoreanDescription(1L, "더미");
        verify(currency, times(1)).updateKoreanDescription(eq("더미"));
    }

    @Test
    @DisplayName("currency 삭제시 currencyId가 존재한다면 삭제 되어야한다.")
    void shouldDeleteCurrencyWhenCurrencyIdExists() {
        when(currencyRepository.findById(eq(1L))).thenReturn(Optional.of(currency));
        currencyService.deleteCurrency(1L);
        verify(currencyRepository, times(1)).delete(currency);
    }
}
