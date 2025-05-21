package com.zonbeozon.market.service;

import com.zonbeozon.exchange.Exchange;
import com.zonbeozon.market.entity.MarketType;
import org.springframework.stereotype.Component;

@Component
public class MarketCodeResolver {
    /**
     * 만일 USDT마켓의 BTC를 조회하고 싶다면
     * 업비트, 빗썸 티커 조회시 마켓 코드명은 ex)USDT-BTC
     * 바이낸스 티커 조회시 마켓 코드명은 ex)BTCUSDT
     */
    public String resolve(Exchange exchange, String symbol, MarketType marketType) {
        return switch (exchange) {
            case Exchange.UPBIT -> resolveUpbitMarketCode(symbol, marketType);
            case Exchange.BITHUMB -> resolveBithumbMarketCode(symbol, marketType);
            case Exchange.BINANCE -> resolveBinanceMarketCode(symbol, marketType);
        };
    }

    private String resolveUpbitMarketCode(String symbol, MarketType marketType) {
        return marketType.toString() + "-" + symbol.toUpperCase();
    }

    private String resolveBithumbMarketCode(String symbol, MarketType marketType) {
        //upbit와 동일
        return resolveUpbitMarketCode(symbol, marketType);
    }

    private String resolveBinanceMarketCode(String symbol, MarketType marketType) {
        return symbol.toUpperCase() + marketType.toString();
    }
}
