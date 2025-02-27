package com.zonbeozon.communityapp.crypto.fetch.currency.coinmarketcap;

import com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap.CMCCurrencyMetaDataFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.dto.CurrencyMetaDataFetchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CMCCurrencyMetaDataFetcherTest {
    @Autowired
    private CMCCurrencyMetaDataFetcher fetcher;

    @Test
    @DisplayName("fetch를 호출하면 올바른 CurrencyMetaDataFetchResult를 반환해야한다.")
    void fetch_ReturnsCurrencyMetaDataWithNonNullFields_WhenValidSymbolsAreProvided() {
        CurrencyMetaDataFetchResult currencyMetaDataFetchResult = fetcher.fetch(List.of("BTC", "ETH"));

        Assertions.assertEquals(2, currencyMetaDataFetchResult.currencyMetaDataList().size());
        currencyMetaDataFetchResult.currencyMetaDataList().forEach(
                currencyMetaData -> {
                    Assertions.assertNotNull(currencyMetaData.logo());
                    Assertions.assertNotNull(currencyMetaData.englishName());
                    Assertions.assertNotNull(currencyMetaData.symbol());
                    Assertions.assertNotNull(currencyMetaData.englishDescription());
                    Assertions.assertNotNull(currencyMetaData.url());
                }
        );
    }
}
