package com.zonbeozon.communityapp.crypto.fetch.currency.coinmarketcap;

import com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap.CMCCurrencyQuotesFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.dto.CurrencyQuotesFetchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CMCCurrencyQuotesFetcherTest {
    @Autowired
    private CMCCurrencyQuotesFetcher fetcher;

    @Test
    @DisplayName("fetch 호출시 올바른 CurrencyQuotesFetchResult가 반환되어야 한다")
    void fetch_ShouldReturnValidCurrencyQuotesFetchResult() {
        CurrencyQuotesFetchResult currencyQuotesFetchResult = fetcher.fetch(List.of("BTC", "ETH"));
        currencyQuotesFetchResult.currencyQuotes().forEach(
                currencyQuote -> {
                    System.out.println(currencyQuote);
                    Assertions.assertNotNull(currencyQuote.circulatingSupply());
                    Assertions.assertNotNull(currencyQuote.marketCap());
                    Assertions.assertNotNull(currencyQuote.rank());
                    Assertions.assertNotNull(currencyQuote.fullyDilutedMarketCap());
                    Assertions.assertNotNull(currencyQuote.symbol());
                    Assertions.assertNotNull(currencyQuote.volume());
                    Assertions.assertNotNull(currencyQuote.totalSupply());
                }
        );
    }
}
