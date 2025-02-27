package com.zonbeozon.communityapp.crpyto.service.market;

import com.zonbeozon.communityapp.crpyto.controller.dto.market.EntireMarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MiniMarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketRequest;
import com.zonbeozon.communityapp.crpyto.controller.dto.ticker.TickerResponse;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketStatus;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.market.repository.MarketRepository;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.exception.MarketException;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.TickerFetcher;
import com.zonbeozon.communityapp.crpyto.fetch.ticker.dto.TickerFetchResult;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.TickerService;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketService {
    private final MarketRepository marketRepository;
    private final MarketTypeResolver marketTypeResolver;
    private final MarketCodeResolver marketCodeResolver;
    private final ExchangeService exchangeService;
    private final TickerService tickerService;
    private final List<TickerFetcher> tickerFetchers;

    @Transactional
    public void addMarkets(Collection<MarketRequest> marketRequests, Currency currency) {
        List<Market> markets = marketRequests.stream()
                .flatMap(marketRequest -> createMarket(marketRequest, currency))
                .toList();
        checkDuplicateMarkets(markets);
        marketRepository.saveAll(markets);
    }

    private Stream<Market> createMarket(MarketRequest marketRequest, Currency currency) {
        //알맞는 exchange가 있는지 검사 및 조회
        Exchange exchange = exchangeService.findByName(marketRequest.exchangeName());
        return marketRequest.pairs().stream().map(pair -> {
            MarketType marketType = marketTypeResolver.resolveFromPair(exchange.getEnglishName(), pair);
            String marketCode = marketCodeResolver.resolve(exchange.getEnglishName(), currency.getSymbol(), marketType);
            return Market.builder()
                    .marketType(marketType)
                    .marketCode(marketCode)
                    .exchange(exchange)
                    .currency(currency)
                    .build();
        });
    }

    private void checkDuplicateMarkets(List<Market> markets) {
        Map<Long, Set<String>> exchangeIdMarketCodeMap = new HashMap<>();
        //기존 db에 있는 마켓 추가
        marketRepository.findAll().forEach(
                market -> exchangeIdMarketCodeMap
                    .computeIfAbsent(market.getExchange().getId(), k -> new HashSet<>())
                    .add(market.getMarketCode()));

        //요청에 있는 마켓 중복 검사
        markets.forEach(market -> {
            exchangeIdMarketCodeMap.computeIfAbsent(market.getExchange().getId(), k -> new HashSet<>());
            Set<String> marketCodes = exchangeIdMarketCodeMap.get(market.getExchange().getId());
            if(marketCodes.contains(market.getMarketCode())) {
                throw new MarketException(ErrorCode.DUPLICATE_MARKET);
            }
            marketCodes.add(market.getMarketCode());
        });
    }

    @Transactional
    public void deleteMarket(Long marketId) {
        Market market = marketRepository.findById(marketId)
                .orElseThrow(() -> new MarketException(ErrorCode.MARKET_NOT_FOUND));
        marketRepository.delete(market);
    }

    @Transactional
    public void changeMarketStatus(Long marketId, String status) {
        Market market = marketRepository.findById(marketId).orElseThrow(() -> new MarketException(ErrorCode.MARKET_NOT_FOUND));
        MarketStatus marketStatus = MarketStatus.parse(status);
        //동일 상태 인지 체크(변경할 필요 없음)
        if(marketStatus.equals(market.getMarketStatus())) return;
        //active 상태로 바꿔야 할때
        if(marketStatus.equals(MarketStatus.ACTIVE)) {
            changeMarketStatusToActive(market);
            return;
        }
        //inactive 상태로 바꿔야 할때
        if(marketStatus.equals(MarketStatus.INACTIVE)) {
            changeMarketStatusToInactive(market);
            return;
        }
        //실행되면 안된다.
        throw new MarketException(ErrorCode.ILLEGAL_MARKET_STATUS);
    }

    @Transactional(readOnly = true)
    public MarketInfoResponse getMarketInfoByCurrency(Long currencyId, FiatType fiatType) {
        List<Market> markets = marketRepository.findActiveMarketsWithTickerAndExchangeByCurrencyId(currencyId);
        Map<String, Map<String, TickerResponse>> exchangeMarketMap = new HashMap<>();
        markets.forEach(market -> {
            String exchangeName = market.getExchange().getEnglishName();
            MarketType marketType = market.getMarketType();
            TickerResponse tickerResponse = createTickerResponse(fiatType , market.getTicker());
            exchangeMarketMap.computeIfAbsent(exchangeName, (key) -> new HashMap<>());
            Map<String, TickerResponse> marketTickerResponseMap = exchangeMarketMap.get(exchangeName);
            marketTickerResponseMap.put(marketType.toString().toLowerCase(), tickerResponse);
        });
        return MarketInfoResponse.builder().currencyId(currencyId).markets(exchangeMarketMap).build();
    }

    private TickerResponse createTickerResponse(FiatType fiatType, Ticker ticker) {
        return switch (fiatType) {
            case KRW -> TickerResponse.builder()
                    .accTradePrice(ticker.getTickerPriceInfoKrw().getAccTradePrice())
                    .lowPrice(ticker.getTickerPriceInfoKrw().getLowPrice())
                    .openingPrice(ticker.getTickerPriceInfoKrw().getOpeningPrice())
                    .highPrice(ticker.getTickerPriceInfoKrw().getHighPrice())
                    .signedChangeRate(ticker.getSignedChangeRate())
                    .signedChangePrice(ticker.getTickerPriceInfoKrw().getSignedChangePrice())
                    .tradePrice(ticker.getTickerPriceInfoKrw().getTradePrice())
                    .build();

            case USD -> TickerResponse.builder()
                    .accTradePrice(ticker.getTickerPriceInfoUsd().getAccTradePrice())
                    .lowPrice(ticker.getTickerPriceInfoUsd().getLowPrice())
                    .openingPrice(ticker.getTickerPriceInfoUsd().getOpeningPrice())
                    .highPrice(ticker.getTickerPriceInfoUsd().getHighPrice())
                    .signedChangeRate(ticker.getSignedChangeRate())
                    .signedChangePrice(ticker.getTickerPriceInfoUsd().getSignedChangePrice())
                    .tradePrice(ticker.getTickerPriceInfoUsd().getTradePrice())
                    .build();
        };
    }

    @Transactional(readOnly = true)
    public EntireMarketInfoResponse getEntireMarketsByExchange(Long exchangeId, FiatType fiatType) {
        Exchange exchange = exchangeService.findById(exchangeId);
        List<Market> markets = marketRepository.findActiveMarketsWithTickerAndCurrencyByExchangeAndMarketType(
                exchange.getId(),
                exchange.getTopPriorityMarketType());

        List<MiniMarketInfoResponse> miniMarketInfoResponse = formatMiniMarketInfoResponse(fiatType, markets);
        return EntireMarketInfoResponse.builder()
                .size(miniMarketInfoResponse.size())
                .currencies(miniMarketInfoResponse)
                .build();
    }

    @Transactional
    public void updateMarkets() {
        tickerFetchers.forEach(tickerFetcher -> {
            List<Market> markets = marketRepository.findActiveMarketsWithTickerByExchange(
                    exchangeService.findByName(tickerFetcher.getExchangeName()));
            if(markets.isEmpty()) {
                log.debug("마켓이 빈 상태이기 때문에 updateMarkets를 종료한다.");
                return;
            }
            List<String> marketCodes = markets.stream()
                    .map(Market::getMarketCode)
                    .toList();
            TickerFetchResult tickerFetchResult = tickerFetcher.fetch(marketCodes);
            updateMarket(tickerFetchResult, markets);
        });
    }

    private void updateMarket(TickerFetchResult tickerFetchResult, List<Market> markets) {
        Map<String, Market> marketCodeMap = markets.stream()
                .collect(Collectors.toMap(Market::getMarketCode, Function.identity()));
        tickerFetchResult.tickers().forEach(
                ticker -> tickerService.updateTicker(marketCodeMap.get(ticker.marketCode()), ticker)
        );
    }

    private List<MiniMarketInfoResponse> formatMiniMarketInfoResponse(FiatType fiatType, List<Market> markets) {
        return switch (fiatType) {
            case KRW -> markets.stream().map(m -> MiniMarketInfoResponse.builder()
                            .currencyId(m.getCurrency().getId())
                            .tradePrice(m.getTicker().getTickerPriceInfoKrw().getTradePrice())
                            .signedChangeRate(m.getTicker().getSignedChangeRate())
                            .build())
                    .toList();
            case USD -> markets.stream().map(m -> MiniMarketInfoResponse.builder()
                            .currencyId(m.getCurrency().getId())
                            .tradePrice(m.getTicker().getTickerPriceInfoUsd().getTradePrice())
                            .signedChangeRate(m.getTicker().getSignedChangeRate())
                            .build())
                    .toList();
        };
    }

    private void changeMarketStatusToActive(Market market) {
        market.updateMarketStatus(MarketStatus.ACTIVE);
    }

    private void changeMarketStatusToInactive(Market market) {
        market.updateMarketStatus(MarketStatus.INACTIVE);
    }
}
