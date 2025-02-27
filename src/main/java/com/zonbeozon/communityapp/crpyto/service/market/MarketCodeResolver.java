package com.zonbeozon.communityapp.crpyto.service.market;

import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.exception.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class MarketCodeResolver {

    /**
     * 만일 USDT마켓의 BTC를 조회하고 싶다면
     * 업비트, 빗썸 티커 조회시 마켓 코드명은 ex)USDT-BTC
     * 바이낸스 티커 조회시 마켓 코드명은 ex)BTCUSDT
     */
    public String resolve(String exchangeName, String symbol, MarketType marketType) {
        return switch (exchangeName) {
            case "upbit" -> resolveUpbitMarketCode(symbol, marketType);
            case "bithumb" -> resolveBithumbMarketCode(symbol, marketType);
            case "binance" -> resolveBinanceMarketCode(symbol, marketType);
            default -> throw new ExchangeException(ErrorCode.EXCHANGE_NOT_FOUND);
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
