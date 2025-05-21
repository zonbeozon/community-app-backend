package com.zonbeozon.currency.loader;

import com.zonbeozon.currency.fetch.CMCMetadataFetcher;
import com.zonbeozon.currency.fetch.CMCQuotesFetcher;
import com.zonbeozon.fiat.entity.FiatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.zonbeozon.currency.test.CMCMetadataDummy.*;
import static com.zonbeozon.currency.test.CMCQuotesDummy.*;
import static com.zonbeozon.currency.test.CommonCurrencyRelatedData.*;
import static com.zonbeozon.currency.test.CurrencyFiatMetricDummy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CurrencyLoaderTest {
    private static final String FILE_PATH = "/kr-currencies-test.json";

    @Mock
    private CMCMetadataFetcher metadataFetcher;
    @Mock
    private CMCQuotesFetcher quotesFetcher;
    @Mock
    private CurrencyFiatConverter fiatConverter;

    private JsonCurrencyLoader jsonCurrencyLoader = new JsonCurrencyLoader(FILE_PATH);
    private MetadataFetchCurrencyLoaderDecorator cmcCurrencyLoader = new MetadataFetchCurrencyLoaderDecorator(jsonCurrencyLoader, metadataFetcher, quotesFetcher);
    private CurrencyLoader currencyLoader = new CurrencyFiatConvertDecorator(cmcCurrencyLoader, fiatConverter);

    @BeforeEach
    void setup() {
        jsonCurrencyLoader = new JsonCurrencyLoader(FILE_PATH);
        cmcCurrencyLoader = new MetadataFetchCurrencyLoaderDecorator(jsonCurrencyLoader, metadataFetcher, quotesFetcher);
        currencyLoader = new CurrencyFiatConvertDecorator(cmcCurrencyLoader, fiatConverter);
    }

    @Test
    @DisplayName("json 파일로 부터 symbol, krDescription, krName 필드가 입력되어야 한다")
    void givenJsonFile_whenLoadingCurrencies_thenFieldsShouldBeSet() {
        List<CurrencyJsonMappingDto> currencies = jsonCurrencyLoader.load();

        assertThat(currencies)
                .hasSize(2)
                .extracting(CurrencyJsonMappingDto::getSymbol, CurrencyJsonMappingDto::getKrName, CurrencyJsonMappingDto::getKrDescription)
                .containsExactlyInAnyOrder(
                        tuple(BTC_SYMBOL, BTC_KR_NAME, BTC_KR_DESCRIPTION),
                        tuple(ETH_SYMBOL, ETH_KR_NAME, ETH_KR_DESCRIPTION)
                );
    }

    @Test
    @DisplayName("cmc loader로 부터 나머지 필드가 입력되어야 한다")
    void givenCmcLoader_whenLoadingCurrencies_thenRemainingFieldsShouldBeSet() {
        cmcFetcherSetup();
        List<CurrencyJsonMappingDto> currencies = cmcCurrencyLoader.load();

        assertThat(currencies)
                .hasSize(2)
                .extracting(
                        CurrencyJsonMappingDto::getEnName,
                        CurrencyJsonMappingDto::getEnDescription,
                        CurrencyJsonMappingDto::getLogo,
                        CurrencyJsonMappingDto::getWebsite,
                        CurrencyJsonMappingDto::getCurrencyRank,
                        CurrencyJsonMappingDto::getCirculatingSupply,
                        CurrencyJsonMappingDto::getTotalSupply
                )
                .containsExactlyInAnyOrder(
                        tuple(BTC_EN_NAME, BTC_EN_DESCRIPTION, BTC_LOGO, BTC_WEBSITE, BTC_RANK, BTC_CIRCULATING_SUPPLY, BTC_TOTAL_SUPPLY),
                        tuple(ETH_EN_NAME, ETH_EN_DESCRIPTION, ETH_LOGO, ETH_WEBSITE, ETH_RANK, ETH_CIRCULATING_SUPPLY, ETH_TOTAL_SUPPLY)
                );

        assertThat(currencies)
                .hasSize(2)
                .flatExtracting(CurrencyJsonMappingDto::getFiatMetricDtoList)
                .contains(BTC_USD_METRIC_DTO, ETH_USD_METRIC_DTO);
    }

    private void cmcFetcherSetup() {
        Set<String> symbols = Set.of(BTC_SYMBOL, ETH_SYMBOL);
        when(metadataFetcher.fetchAsync(symbols))
                .thenReturn(CompletableFuture.completedFuture(CMC_METADATA_RESPONSE));
        when(quotesFetcher.fetchAsync(symbols))
                .thenReturn(CompletableFuture.completedFuture(CMC_QUOTES_RESPONSE));
    }


    @Test
    @DisplayName("fiatConvertDecorator로 부터 FiatType Enum에 등록된 모든 통화 기준으로 fiatMetric이 추가되어야 한다.")
    void givenFiatConvertDecorator_whenLoadingCurrencies_thenFiatMetricShouldBeAdded() {
        cmcFetcherSetup();
        fiatConverterSetup();
        List<CurrencyJsonMappingDto> currencies = currencyLoader.load();

        assertThat(currencies)
                .hasSize(2)
                .flatExtracting(CurrencyJsonMappingDto::getFiatMetricDtoList)
                .contains(BTC_USD_METRIC_DTO, BTC_KRW_METRIC_DTO, ETH_USD_METRIC_DTO, ETH_KRW_METRIC_DTO);
    }

    private void fiatConverterSetup() {
        when(fiatConverter.convertToAllFiats(Map.of(FiatType.USD, ETH_USD_METRIC_DTO))).thenReturn(Map.of(FiatType.USD, ETH_USD_METRIC_DTO, FiatType.KRW, ETH_KRW_METRIC_DTO));
        when(fiatConverter.convertToAllFiats(Map.of(FiatType.USD, BTC_USD_METRIC_DTO))).thenReturn(Map.of(FiatType.USD, BTC_USD_METRIC_DTO, FiatType.KRW, BTC_KRW_METRIC_DTO));
    }
}
