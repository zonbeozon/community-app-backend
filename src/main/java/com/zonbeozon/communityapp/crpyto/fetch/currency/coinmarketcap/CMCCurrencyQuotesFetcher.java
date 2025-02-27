package com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap;

import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.CurrencyQuotesFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap.dto.CMCCurrencyQuotesRequest;
import com.zonbeozon.communityapp.crpyto.domain.currency.dto.CurrencyQuote;
import com.zonbeozon.communityapp.crpyto.fetch.currency.dto.CurrencyQuotesFetchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static com.zonbeozon.communityapp.crpyto.fetch.currency.coinmarketcap.CMCUrls.*;

@Component
public class CMCCurrencyQuotesFetcher implements CurrencyQuotesFetcher {
    private final DefaultRestFetcher restFetcher;
    private final String key;

    @Autowired
    public CMCCurrencyQuotesFetcher(
            DefaultRestFetcher restFetcher,
            @Value("${cmc.key}") String key
    ) {
        this.restFetcher = restFetcher;
        this.key = key;
    }

    @Override
    public CurrencyQuotesFetchResult fetch(Iterable<String> symbols) {
        String joinedSymbol = String.join(",", symbols);
        CMCCurrencyQuotesRequest cmcCurrencyQuotesRequest = restFetcher.fetchWithParam(
                BASE_URL + QUOTES_RESOURCE_URL,
                createParamMap(joinedSymbol, QUOTES_AUX),
                new ParameterizedTypeReference<>() {},
                createHeaderMap()
        );

        return convert(cmcCurrencyQuotesRequest);
    }

    private Map<String, String> createHeaderMap() {
        return Map.of("X-CMC_PRO_API_KEY", key);
    }

    private CurrencyQuotesFetchResult convert(CMCCurrencyQuotesRequest cmcCurrencyQuotesRequest) {
        List<CurrencyQuote> currencyQuotes = cmcCurrencyQuotesRequest.currencyQuoteMap().values().stream()
                .map(v -> {
                    CMCCurrencyQuotesRequest.CMCCurrencyQuotePriceDetail currencyQuotePriceDetail = v.cmcCurrencyQuoteUsdPriceDetail().cmcCurrencyQuotePriceDetail();
                    return CurrencyQuote.builder()
                            .symbol(v.symbol())
                            .rank(v.rank())
                            .circulatingSupply(v.circulatingSupply())
                            .totalSupply(v.totalSupply())
                            .volume(currencyQuotePriceDetail.volume())
                            .fullyDilutedMarketCap(currencyQuotePriceDetail.fullyDilutedMarketCap())
                            .marketCap(currencyQuotePriceDetail.marketCap())
                            .build();
                }).toList();

        return new CurrencyQuotesFetchResult(currencyQuotes);
    }

    private static MultiValueMap<String,String> createParamMap(String joinedSymbol, String aux) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("symbol", joinedSymbol);
        params.add("aux", aux);
        return params;
    }
}
