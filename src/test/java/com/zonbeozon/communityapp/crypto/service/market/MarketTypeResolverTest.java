package com.zonbeozon.communityapp.crypto.service.market;

import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.crpyto.exception.MarketException;
import com.zonbeozon.communityapp.crpyto.service.market.MarketTypeResolver;
import com.zonbeozon.communityapp.crpyto.service.market.SimpleMarketTypeResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarketTypeResolverTest {
    private MarketTypeResolver marketTypeResolver = new SimpleMarketTypeResolver();

    @Test
    @DisplayName("resolve 호출시 지원하지 않는 거래소가 주어지면 예외를 던진다")
    void shouldThrowExceptionWhenExchangeIsUnsupported() {
        assertThrows(
                ExchangeException.class,
                () -> marketTypeResolver.resolveFromPair("downbit", "usdt"));
    }

    @Test
    @DisplayName("resolve 호출시 올바른 마켓 타입과 거래소 이름이 주어지면 마켓 타입을 리턴한다")
    void shouldReturnMarketTypeWhenValidMarketTypeIsProvided() {
        MarketType marketType = marketTypeResolver.resolveFromPair("upbit", "krw");
        assertEquals(MarketType.KRW, marketType);
    }

    @Test
    @DisplayName("resolve 호출시 거래소에서 지원하지 않는 마켓 타입이 주어지면 예외를 던진다")
    void shouldThrowExceptionWhenMarketTypeIsNotSupportedByExchange() {
        assertThrows(
                MarketException.class,
                () -> marketTypeResolver.resolveFromPair("upbit", "usdt"));
    }

    @Test
    @DisplayName("resolve 호출시 없는 마켓 타입이 주어지면 예외를 던진다")
    void shouldThrowExceptionWhenMarketTypeIsInvalid() {
        assertThrows(
                MarketException.class,
                () -> marketTypeResolver.resolveFromPair("upbit", "usd"));
    }
}
