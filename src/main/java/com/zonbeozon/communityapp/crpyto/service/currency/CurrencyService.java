package com.zonbeozon.communityapp.crpyto.service.currency;

import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyRequest;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyStatsInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.MiniCurrenciesResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketAddRequest;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketRequest;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.currency.CurrencyStatsInfo;
import com.zonbeozon.communityapp.crpyto.domain.currency.repository.CurrencyRepository;
import com.zonbeozon.communityapp.crpyto.exception.CurrencyException;
import com.zonbeozon.communityapp.crpyto.fetch.currency.CurrencyMetaDataFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.currency.CurrencyQuotesFetcher;
import com.zonbeozon.communityapp.crpyto.domain.currency.dto.CurrencyMetaData;
import com.zonbeozon.communityapp.crpyto.domain.currency.dto.CurrencyQuote;
import com.zonbeozon.communityapp.crpyto.service.market.MarketService;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRate;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import com.zonbeozon.communityapp.exchangerate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMetaDataFetcher currencyMetaDataFetcher;
    private final CurrencyQuotesFetcher currencyQuotesFetcher;
    private final MarketService marketService;
    private final ExchangeRateService exchangeRateService;

    /**
     * 자세한 설명은 프로젝트 notion의 backend logic의 inital add currecny를 참조
     */
    @Transactional
    public void addCurrency(CurrencyRequest currencyRequest) {
        if (isDuplicateSymbol(currencyRequest.symbol()))
            throw new CurrencyException(ErrorCode.DUPLICATE_CURRENCY_SYMBOL);
        CurrencyMetaData currencyMetaData = currencyMetaDataFetcher.fetch(Collections.singletonList(currencyRequest.symbol()))
                .currencyMetaDataList()
                .getFirst();
        CurrencyQuote currencyQuote = currencyQuotesFetcher.fetch(Collections.singletonList(currencyRequest.symbol()))
                .currencyQuotes()
                .getFirst();
        Currency currency = currencyRepository.save(createCurrency(currencyRequest, currencyMetaData, currencyQuote));
        marketService.addMarkets(currencyRequest.markets(), currency);
    }

    @Transactional
    public void addMarket(MarketAddRequest marketAddRequest) {
        Currency currency = currencyRepository.findById(marketAddRequest.currencyId())
                .orElseThrow(() -> new CurrencyException(ErrorCode.CURRENCY_NOT_FOUND));

        MarketRequest marketRequest = MarketRequest.builder()
                .pairs(Collections.singletonList(marketAddRequest.pair()))
                .exchangeName(marketAddRequest.exchangeName())
                .build();

        marketService.addMarkets(Collections.singletonList(marketRequest), currency);
    }

    @Transactional
    public void updateCurrencyQuotes() {
        Map<String, Currency> currencyMap = currencyRepository.findAll().stream()
                .collect(Collectors.toMap(Currency::getSymbol, Function.identity()));
        if(currencyMap.isEmpty()) return;
        Map<String, CurrencyQuote> fetchedCurrencyQuoteMap = currencyQuotesFetcher.fetch(currencyMap.keySet())
                .currencyQuotes().stream()
                .collect(Collectors.toMap(CurrencyQuote::symbol, Function.identity()));

        currencyMap.forEach(
                (symbol, currency) -> {
                    CurrencyQuote currencyQuote = fetchedCurrencyQuoteMap.get(symbol);
                    currency.updateQuote(
                            currencyQuote.rank(),
                            createCurrencyStatsInfoKrw(currencyQuote),
                            createCurrencyStatsInfo(currencyQuote));
                });
    }

    @Transactional
    public void updateEnglishDescription(long currencyId, String description) {
        currencyRepository.findById(currencyId)
                .orElseThrow(() -> new CurrencyException(ErrorCode.CURRENCY_NOT_FOUND))
                .updateEnglishDescription(description);
    }

    @Transactional
    public void updateKoreanDescription(long currencyId, String description) {
        currencyRepository.findById(currencyId)
                .orElseThrow(() -> new CurrencyException(ErrorCode.CURRENCY_NOT_FOUND))
                .updateKoreanDescription(description);
    }

    @Transactional
    public void deleteCurrency(long currencyId) {
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new CurrencyException(ErrorCode.CURRENCY_NOT_FOUND));
        currencyRepository.delete(currency);
    }

    @Transactional(readOnly = true)
    public CurrencyResponse getCurrencyResponse(long currencyId, String fiatType) {
        FiatType parsedFiatType = FiatType.parse(fiatType);
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new CurrencyException(ErrorCode.CURRENCY_NOT_FOUND));
        return CurrencyResponse.builder()
                .id(currency.getId())
                .rank(currency.getCurrencyRank())
                .koreanDescription(currency.getKoreanDescription())
                .englishDescription(currency.getEnglishDescription())
                .logo(currency.getLogo())
                .website(currency.getWebsite())
                .koreanName(currency.getKoreanName())
                .englishName(currency.getEnglishName())
                .symbol(currency.getSymbol())
                .currencyStatsInfo(createCurrencyStatsInfoResponse(currency, parsedFiatType))
                .build();
    }

    @Transactional(readOnly = true)
    public MiniCurrenciesResponse getAllMiniCurrenciesResponse(String fiatType) {
        FiatType parsedFiatType = FiatType.parse(fiatType);
        List<MiniCurrenciesResponse.SingleMiniCurrencyResponse> currencies = currencyRepository.findAll().stream()
                .map(currency -> MiniCurrenciesResponse.SingleMiniCurrencyResponse.builder()
                        .id(currency.getId())
                        .symbol(currency.getSymbol())
                        .rank(currency.getCurrencyRank())
                        .logo(currency.getLogo())
                        .koreanName(currency.getKoreanName())
                        .englishName(currency.getEnglishName())
                        .currencyStatsInfo(createCurrencyStatsInfoResponse(currency, parsedFiatType))
                        .build()
                )
                .toList();
        int size = currencies.size();
        return MiniCurrenciesResponse.builder()
                .currencies(currencies)
                .size(size)
                .build();
    }

    private CurrencyStatsInfoResponse createCurrencyStatsInfoResponse(Currency currency, FiatType fiatType) {
        return switch (fiatType) {
            case USD -> CurrencyStatsInfoResponse.builder()
                    .volume(currency.getCurrencyStatsInfoUsd().getVolume())
                    .totalSupply(currency.getCurrencyStatsInfoUsd().getTotalSupply())
                    .fullyDilutedMarketCap(currency.getCurrencyStatsInfoUsd().getFullyDilutedMarketCap())
                    .marketCap(currency.getCurrencyStatsInfoUsd().getMarketCap())
                    .circulatingSupply(currency.getCurrencyStatsInfoUsd().getCirculatingSupply())
                    .build();
            case KRW -> CurrencyStatsInfoResponse.builder()
                    .volume(currency.getCurrencyStatsInfoKrw().getVolume())
                    .totalSupply(currency.getCurrencyStatsInfoKrw().getTotalSupply())
                    .fullyDilutedMarketCap(currency.getCurrencyStatsInfoKrw().getFullyDilutedMarketCap())
                    .marketCap(currency.getCurrencyStatsInfoKrw().getMarketCap())
                    .circulatingSupply(currency.getCurrencyStatsInfoKrw().getCirculatingSupply())
                    .build();
        };
    }

    public Currency findById(long id) {
        return currencyRepository.findById(id)
                .orElseThrow(() -> new CurrencyException(ErrorCode.CURRENCY_NOT_FOUND));
    }

    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    private boolean isDuplicateSymbol(String symbol) {
        return currencyRepository.existsBySymbol(symbol);
    }

    private Currency createCurrency(
            CurrencyRequest currencyRequest,
            CurrencyMetaData currencyMetaData,
            CurrencyQuote currencyQuote
    ) {
        return Currency.builder()
                .englishName(currencyMetaData.englishName())
                .koreanName(currencyRequest.koreanName())
                .englishDescription(currencyMetaData.englishDescription())
                .koreanDescription("")
                .symbol(currencyMetaData.symbol().toUpperCase())
                .website(currencyMetaData.url())
                .logo(currencyMetaData.logo())
                .currencyRank(currencyQuote.rank())
                .currencyStatsInfoKrw(createCurrencyStatsInfoKrw(currencyQuote))
                .currencyStatsInfoUsd(createCurrencyStatsInfo(currencyQuote))
                .build();
    }

    private CurrencyStatsInfo createCurrencyStatsInfo(CurrencyQuote currencyQuote) {
        return CurrencyStatsInfo.builder()
                .volume(currencyQuote.volume())
                .totalSupply(currencyQuote.totalSupply())
                .fullyDilutedMarketCap(currencyQuote.fullyDilutedMarketCap())
                .marketCap(currencyQuote.marketCap())
                .circulatingSupply(currencyQuote.circulatingSupply())
                .build();
    }

    private CurrencyStatsInfo createCurrencyStatsInfoKrw(CurrencyQuote currencyQuote) {
        ExchangeRate usdExchangeRate = exchangeRateService.getLatestExchangeRate(ExchangeRateCode.USD);
        return CurrencyStatsInfo.builder()
                .volume(ExchangeRateService.calculateOtherCurrencyToKrw(currencyQuote.volume(), usdExchangeRate))
                .totalSupply(ExchangeRateService.calculateOtherCurrencyToKrw(currencyQuote.totalSupply(), usdExchangeRate))
                .fullyDilutedMarketCap(ExchangeRateService.calculateOtherCurrencyToKrw(currencyQuote.fullyDilutedMarketCap(), usdExchangeRate))
                .marketCap(ExchangeRateService.calculateOtherCurrencyToKrw(currencyQuote.marketCap(), usdExchangeRate))
                .circulatingSupply(ExchangeRateService.calculateOtherCurrencyToKrw(currencyQuote.circulatingSupply(), usdExchangeRate))
                .build();
    }
}


