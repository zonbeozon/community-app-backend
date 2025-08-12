package com.zonbeozon.currency.updater;

import com.zonbeozon.crypto.entity.CurrencyQuote;
import com.zonbeozon.global.AbstractFetchBasedUpdater;
import com.zonbeozon.global.fetch.FetchManager;
import com.zonbeozon.currency.CurrencyHolder;
import com.zonbeozon.currency.CurrencyHolderSupplier;
import com.zonbeozon.crypto.entity.Currency;
import com.zonbeozon.currency.fetch.CurrencyFetchContext;
import com.zonbeozon.currency.fetch.CurrencyQuotesFetchData;
import com.zonbeozon.currency.fetch.CurrencyQuotesFetchResult;
import com.zonbeozon.currency.service.CurrencyFiatMetricService;
import com.zonbeozon.fiat.ExpandToMultiFiat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class CurrencyUpdater
        extends AbstractFetchBasedUpdater<CurrencyFetchContext, CurrencyQuotesFetchResult>
{
    private final CurrencyHolderSupplier currencyHolderSupplier;
    private final CurrencyFiatMetricService currencyFiatMetricService;

    @Autowired
    public CurrencyUpdater(
            FetchManager<CurrencyFetchContext, CurrencyQuotesFetchResult> fetchManager,
            CurrencyHolderSupplier currencyHolderSupplier,
            CurrencyFiatMetricService currencyFiatMetricService
    ) {
        super(fetchManager);
        this.currencyHolderSupplier = currencyHolderSupplier;
        this.currencyFiatMetricService = currencyFiatMetricService;
    }


    @Transactional
    @ExpandToMultiFiat
    public CurrencyHolder update() {
        CurrencyHolder currencyHolder = currencyHolderSupplier.get();
        consumeFetchResult(super.update(currencyHolder), currencyHolder);
        return currencyHolder;
    }

    public void consumeFetchResult(CurrencyQuotesFetchResult fetchResult, CurrencyHolder currencyHolder) {
        fetchResult.getQuotesFetchData().forEach(
                currencyQuotesFetchData -> currencyHolder.apply(
                        currencyQuotesFetchData.symbol(),
                        (currency) -> {
                            currency.updateQuotes(
                                    currencyQuotesFetchData.rank(),
                                    currencyQuotesFetchData.circulatingSupply(),
                                    currencyQuotesFetchData.totalSupply()
                            );
                            ifFiatMetricExistDoUpdateOrAdd(currency, currencyQuotesFetchData);
                        }
                )
        );
    }

    private void ifFiatMetricExistDoUpdateOrAdd(Currency currency, CurrencyQuotesFetchData quotesFetchData) {
        CurrencyQuote newCurrencyQuote = createCurrencyFiatMetric(currency, quotesFetchData);
        Optional<CurrencyQuote> optCurrencyFiatMetric = currency.getCurrencyQuotes().stream()
                .filter(marketFiatMetric -> marketFiatMetric.getFiatType() == newCurrencyQuote.getFiatType())
                .findAny();
        if(optCurrencyFiatMetric.isPresent()) {
            optCurrencyFiatMetric.get().update(newCurrencyQuote);
            return;
        }
        currencyFiatMetricService.addCurrencyFiatMetric(newCurrencyQuote);
    }

    private CurrencyQuote createCurrencyFiatMetric(Currency currency, CurrencyQuotesFetchData quotesFetchData) {
        return CurrencyQuote.create(
                quotesFetchData.fiatType(),
                currency,
                quotesFetchData.marketCap(),
                quotesFetchData.fullyDilutedMarketCap(),
                quotesFetchData.volume()
        );
    }
}
