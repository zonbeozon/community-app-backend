package com.zonbeozon.communityapp.crpyto.service.market;

import com.zonbeozon.communityapp.crpyto.controller.dto.market.EntireMarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MarketResponseFactory {
    private final MarketService marketService;
    private final ExchangeService exchangeService;
    private final CurrencyService currencyService;

    public Map<Exchange, Map<FiatType, EntireMarketInfoResponse>> createEntireMarketInfoResponsesMap() {
        return exchangeService.findAll().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        exchange -> Arrays.stream(FiatType.values())
                                .collect(Collectors.toMap(
                                        Function.identity(),
                                        fiatType -> marketService.getEntireMarketsByExchange(exchange.getId(), fiatType)
                                ))
                ));
    }

    public Map<Currency, Map<FiatType, MarketInfoResponse>> createSingleMarketInfoResponsesMap() {
        return currencyService.findAll().stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        currency -> Arrays.stream(FiatType.values())
                                .collect(Collectors.toMap(
                                        Function.identity(),
                                        fiatType -> marketService.getMarketInfoByCurrency(currency.getId(), fiatType)
                                ))
                ));
    }
}
