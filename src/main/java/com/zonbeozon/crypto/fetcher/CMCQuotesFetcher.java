package com.zonbeozon.crypto.fetcher;

import com.zonbeozon.crypto.dto.CurrencyQuotesDto;
import com.zonbeozon.fiat.entity.FiatType;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class CMCQuotesFetcher extends CMCAbstractFetcher implements QuotesFetcher {
    private static final String QUOTES_RESOURCE_URL = "/v1/cryptocurrency/quotes/latest";
    private static final String QUOTES_AUX = "cmc_rank,circulating_supply,max_supply,total_supply";

    @Autowired
    public CMCQuotesFetcher(
            final @Value("${cmc.key}") String key,
            final RestClient.Builder restClientBuilder,
            final Validator validator
    ) {
        super(key, restClientBuilder, validator);
    }

    @Override
    public Set<CurrencyQuotesDto> fetch(Collection<String> symbols) {
        CMCQuotesResponse response = super.fetch(symbols, CMCQuotesResponse.class);
        return response.quoteMap().values().stream()
                .map(quotes -> new CurrencyQuotesDto(
                        quotes.symbol(),
                        FiatType.USD,
                        quotes.rank(),
                        quotes.circulatingSupply(),
                        quotes.totalSupply(),
                        quotes.quotesDetails().quoteUsdDetails().volume(),
                        quotes.quotesDetails().quoteUsdDetails().marketCap(),
                        quotes.quotesDetails().quoteUsdDetails().fullyDilutedMarketCap()
                )).collect(Collectors.toSet());
    }

    @Override
    protected String getPath() {
        return QUOTES_RESOURCE_URL;
    }

    @Override
    protected String getAux() {
        return QUOTES_AUX;
    }

    @Override
    public String toString() {
        return "CMCCurrencyQuotesFetcher" + this.hashCode();
    }
}
