package com.zonbeozon.market;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.Market;
import com.zonbeozon.market.entity.MarketType;

import java.math.BigDecimal;

public class MarketFixture {
    public static Market ETH_KRW_UPBIT_MARKET = MarketTestUtils.copyMarket(MarketTestUtils.createMarket(
            "KRW-ETH",
            Exchange.UPBIT,
            MarketType.KRW,
            BigDecimal.ZERO
    ));

    public static Market BTC_KRW_UPBIT_MARKET = MarketTestUtils.copyMarket(MarketTestUtils.createMarket(
            "KRW-BTC",
            Exchange.UPBIT,
            MarketType.KRW,
            BigDecimal.ZERO
    ));

    public static Market ETH_USDT_BINANCE_MARKET = MarketTestUtils.copyMarket(MarketTestUtils.createMarket(
            "ETHUSDT",
            Exchange.BINANCE,
            MarketType.USDT,
            BigDecimal.ZERO
    ));

    public static Market BTC_USDT_BINANCE_MARKET = MarketTestUtils.copyMarket(MarketTestUtils.createMarket(
            "BTCUSDT",
            Exchange.BINANCE,
            MarketType.USDT,
            BigDecimal.ZERO
    ));
}
