package com.zonbeozon.communityapp.crpyto.service.market;

import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.crpyto.exception.MarketException;
import com.zonbeozon.communityapp.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 해당 클레스는 단순한 마켓타입 리졸버이다
 * 현재 기준
 * 업비트, 빗썸등 원화거래소는 krw페어만 지원
 * 바이낸스등 해외거래소는 usdt페어만 지원
 */
@Component
@RequiredArgsConstructor
public class SimpleMarketTypeResolver implements MarketTypeResolver {
    @Override
    public MarketType resolveFromPair(String exchangeName, String pair) {
        try {
            MarketType marketType = MarketType.valueOf(pair.toUpperCase());
            validateMarketType(exchangeName, marketType);
            return marketType;

        } catch (IllegalArgumentException e) {
            throw new MarketException(ErrorCode.ILLEGAL_MARET_CODE);
        }
    }

    @Override
    public MarketType resolveFromMarketCode(String marketCode) {
        return Arrays.stream(MarketType.values())
                .filter(marketType -> marketCode.toUpperCase().contains(marketType.name()))
                .findAny()
                .orElseThrow(() -> new ExchangeException(ErrorCode.ILLEGAL_MARET_CODE));
    }

    private void validateMarketType(String exchangeName, MarketType marketType) {
        if(exchangeName.equals("upbit") || exchangeName.equals("bithumb")) {
            if(!marketType.equals(MarketType.KRW)) throw new MarketException(ErrorCode.ILLEGAL_MARET_CODE);
            return;
        }

        if(exchangeName.equals("binance")) {
            if(!marketType.equals(MarketType.USDT)) throw new MarketException(ErrorCode.ILLEGAL_MARET_CODE);
            return;
        }

        throw new ExchangeException(ErrorCode.EXCHANGE_NOT_FOUND);
    }
}
