package com.zonbeozon.communityapp.crypto;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerRequest;

import java.time.LocalDateTime;

public class ExampleObjectFactory {
    public static ExchangeRequest createExampleExchangeRequest(
            String englishName,
            String koreanName,
            String description) {
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setEnglishName(englishName);
        exchangeRequest.setKoreanName(koreanName);
        exchangeRequest.setDescription(description);
        return exchangeRequest;
    }

    public static Exchange createExampleExchange(
            long id,
            String englishName,
            String koreanName,
            String description
    ) {
        Exchange exchange = Exchange.fromDto(createExampleExchangeRequest(
                englishName,
                koreanName,
                description));
        exchange.setId(id);
        return exchange;
    }

    public static Exchange createExampleExchange(
            String englishName,
            String koreanName,
            String description
    ) {
        return Exchange.fromDto(createExampleExchangeRequest(
                englishName,
                koreanName,
                description));
    }

    public static MarketRequest createExampleMarketRequest(
            String marketCode,
            String koreanName,
            String englishName) {
        MarketRequest marketRequest = new MarketRequest();
        marketRequest.setMarketCode(marketCode);
        marketRequest.setKoreanName(koreanName);
        marketRequest.setEnglishName(englishName);
        return marketRequest;
    }

    public static Market createExampleMarket(
            long id,
            String marketCode,
            String koreanName,
            String englishName
    ) {
        Market market = Market.fromDto(createExampleMarketRequest(
                marketCode,
                koreanName,
                englishName
        ));
        market.setId(id);
        return market;
    }

    public static Market createExampleMarket(
            String marketCode,
            String koreanName,
            String englishName
    ) {
        return Market.fromDto(createExampleMarketRequest(
                marketCode,
                koreanName,
                englishName
        ));
    }

    public static TickerRequest createExampleTickerRequest(
            String marketCode,
            double openingPrice,
            double highPrice,
            double lowPrice,
            double tradePrice,
            double signedChangePrice,
            double signedChangeRate,
            double accTracePrice,
            LocalDateTime updatedAt
    ) {
        TickerRequest tickerRequest = new TickerRequest();
        tickerRequest.setMarketCode(marketCode);
        tickerRequest.setOpeningPrice(openingPrice);
        tickerRequest.setHighPrice(highPrice);
        tickerRequest.setLowPrice(lowPrice);
        tickerRequest.setTradePrice(tradePrice);
        tickerRequest.setSignedChangePrice(signedChangePrice);
        tickerRequest.setSignedChangeRate(signedChangeRate);
        tickerRequest.setAccTradePrice(accTracePrice);
        tickerRequest.setUpdatedAt(updatedAt);
        return tickerRequest;
    }

    public static Ticker createExampleTicker(
            long id,
            String marketCode,
            double openingPrice,
            double highPrice,
            double lowPrice,
            double tradePrice,
            double signedChangePrice,
            double signedChangeRate,
            double accTracePrice,
            LocalDateTime updatedAt
    ) {
        Ticker ticker = Ticker.fromDto(createExampleTickerRequest(
                marketCode,
                openingPrice,
                highPrice,
                lowPrice,
                tradePrice,
                signedChangePrice,
                signedChangeRate,
                accTracePrice,
                updatedAt
        ));
        ticker.setId(id);
        return ticker;
    }

    public static Ticker createExampleTicker(
            String marketCode,
            double openingPrice,
            double highPrice,
            double lowPrice,
            double tradePrice,
            double signedChangePrice,
            double signedChangeRate,
            double accTracePrice,
            LocalDateTime updatedAt
    ) {
        return Ticker.fromDto(createExampleTickerRequest(
                marketCode,
                openingPrice,
                highPrice,
                lowPrice,
                tradePrice,
                signedChangePrice,
                signedChangeRate,
                accTracePrice,
                updatedAt
        ));
    }
}
