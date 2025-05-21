package com.zonbeozon.market;

import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketFiatMetric;
import com.zonbeozon.market.entity.MarketType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class MarketTestUtils {

    private MarketTestUtils() {
    }

    public static Market createMarket(
            String marketCode,
            Exchange exchange,
            MarketType marketType,
            BigDecimal signedChangeRate
    ) {
        try {
            Constructor<Market> constructor = Market.class.getDeclaredConstructor();

            constructor.setAccessible(true);
            Market market = constructor.newInstance();

            setField(market, "marketCode", marketCode);
            setField(market, "exchange", exchange);
            setField(market, "marketType", marketType);
            setField(market, "signedChangeRate", signedChangeRate);
            return market;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Market copyMarket(Market market) {
        Market copiedMarket = createMarket(
                market.getMarketCode(),
                market.getExchange(),
                market.getMarketType(),
                market.getSignedChangeRate()
        );
        Set<MarketFiatMetric> copiedMarketFiatMetric = copyMarketFiatMetrics(market.getMarketFiatMetrics(), copiedMarket);
        setMarketFiatMetrics(copiedMarket, copiedMarketFiatMetric);
        return copiedMarket;
    }

    private static Set<MarketFiatMetric> copyMarketFiatMetrics(Set<MarketFiatMetric> marketFiatMetrics, Market market) {
        return marketFiatMetrics.stream().map(marketFiatMetric -> MarketFiatMetric.create(
                market,
                marketFiatMetric.getFiatType(),
                marketFiatMetric.getOpeningPrice(),
                marketFiatMetric.getHighPrice(),
                marketFiatMetric.getLowPrice(),
                marketFiatMetric.getTradePrice(),
                marketFiatMetric.getSignedChangePrice(),
                marketFiatMetric.getAccTradePrice()
        )).collect(Collectors.toSet());
    }

    public static void setId(
            String id,
            Market market
    ) {
        setField(market, "id", id);
    }

    public static void setMarketFiatMetrics(
            Market market,
            Set<MarketFiatMetric> marketFiatMetrics
    ) {
        setField(market, "fiatMetrics", marketFiatMetrics);
    }

    public static void setCurrency(
            Market market,
            Currency currency
    ) {
        setField(market, "currency", currency);
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
