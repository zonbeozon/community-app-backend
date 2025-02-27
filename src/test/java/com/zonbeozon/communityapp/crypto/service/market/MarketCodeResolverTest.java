package com.zonbeozon.communityapp.crypto.service.market;

import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.crpyto.service.market.MarketCodeResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarketCodeResolverTest {
    private final MarketCodeResolver marketCodeResolver = new MarketCodeResolver();

    @Test
    @DisplayName("resolve 호출시 존재하지 않는 거래소 명이 입력되면 예외를 던진다")
    void shouldThrowExceptionWhenExchangeDoesNotExist() {
        assertThrows(
                ExchangeException.class,
                () -> marketCodeResolver.resolve("downbit", "BTC", MarketType.KRW));
    }

    @Test
    @DisplayName("심볼이 BTC이고 마켓 타입이 KRW라면 resolve 호출시 upbit 거래소라면 KRW-BTC로 마켓 코드가 반환되어야 한다")
    void shouldReturnMarketCodeForKRWWhenSymbolIsBTCAndExchangeIsUpbit() {
        String marketCode = marketCodeResolver.resolve("upbit", "BTC", MarketType.KRW);
        assertEquals("KRW-BTC", marketCode);
    }

    @Test
    @DisplayName("심볼이 BTC이고 마켓 타입이 USDT라면 resolve 호출시 binance 거래소라면 BTCUSDT로 마켓 코드가 반환되어야 한다")
    void shouldReturnMarketCodeForUSDTWhenSymbolIsBTCAndExchangeIsBinance() {
        String marketCode = marketCodeResolver.resolve("binance", "BTC", MarketType.USDT);
        assertEquals("BTCUSDT", marketCode);
    }
}
