package com.zonbeozon.currency.test;

import com.zonbeozon.currency.fetch.CMCQuotesResponse;

import java.util.Map;

import static com.zonbeozon.currency.test.CommonCurrencyRelatedData.*;

public class CMCQuotesDummy {
    // BTC Quote
    public static final CMCQuotesResponse.QuotePriceDetail BTC_USD_DETAIL = new CMCQuotesResponse.QuotePriceDetail(
            BTC_USD_VOLUME_24H,
            BTC_USD_MARKET_CAP,
            BTC_USD_FULLY_DILUTED_MARKET_CAP
    );

    public static final CMCQuotesResponse.CMCQuote BTC_CMC_QUOTE = new CMCQuotesResponse.CMCQuote(
            BTC_SYMBOL,
            BTC_RANK,
            BTC_CIRCULATING_SUPPLY,
            BTC_TOTAL_SUPPLY,
            new CMCQuotesResponse.USDDetails(BTC_USD_DETAIL)
    );

    // ETH Quote
    public static final CMCQuotesResponse.QuotePriceDetail ETH_USD_DETAIL = new CMCQuotesResponse.QuotePriceDetail(
            ETH_USD_VOLUME_24H,
            ETH_USD_MARKET_CAP,
            ETH_USD_FULLY_DILUTED_MARKET_CAP
    );

    public static final CMCQuotesResponse.CMCQuote ETH_CMC_QUOTE = new CMCQuotesResponse.CMCQuote(
            ETH_SYMBOL,
            ETH_RANK,
            ETH_CIRCULATING_SUPPLY,
            ETH_TOTAL_SUPPLY,
            new CMCQuotesResponse.USDDetails(ETH_USD_DETAIL)
    );



    public static final CMCQuotesResponse CMC_QUOTES_RESPONSE = new CMCQuotesResponse(
            Map.of(
                    BTC_SYMBOL, BTC_CMC_QUOTE,
                    ETH_SYMBOL, ETH_CMC_QUOTE
            )
    );
}
