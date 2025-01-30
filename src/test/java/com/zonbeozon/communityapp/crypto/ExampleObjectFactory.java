package com.zonbeozon.communityapp.crypto;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketType;
import com.zonbeozon.communityapp.crpyto.domain.market.dto.MarketRequest;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerRequest;
import org.yaml.snakeyaml.error.Mark;

import java.time.LocalDateTime;

public class ExampleObjectFactory {
    public static Exchange createExchange(
            String englishName,
            String koreanName,
            String description
    ) {
        return Exchange.fromDto(ExchangeRequest.builder()
                        .englishName(englishName)
                        .koreanName(koreanName)
                        .description(description)
                        .build());
    }

    public static Market createMarket(
            String marketCode,
            String koreanName,
            String englishName,
            MarketType marketType
    ) {
        return Market.fromDto(MarketRequest.builder()
                        .koreanName(koreanName)
                        .marketType(marketType)
                        .englishName(englishName)
                        .marketCode(marketCode)
                        .build());
    }

    public static Ticker createTicker(
            String marketCode,
            double openingPrice,
            double highPrice,
            double lowPrice,
            double tradePrice,
            double signedChangePrice,
            double signedChangeRate,
            double accTracePrice
    ) {
        return Ticker.fromDto(TickerRequest.builder()
                        .marketCode(marketCode)
                        .openingPrice(openingPrice)
                        .highPrice(highPrice)
                        .lowPrice(lowPrice)
                        .tradePrice(tradePrice)
                        .signedChangePrice(signedChangePrice)
                        .signedChangeRate(signedChangeRate)
                        .accTradePrice(accTracePrice)
                .build());
    }
}
