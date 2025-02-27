package com.zonbeozon.communityapp.crpyto.service;

import com.zonbeozon.communityapp.crpyto.domain.market.Market;
import com.zonbeozon.communityapp.crpyto.domain.ticker.Ticker;
import com.zonbeozon.communityapp.crpyto.domain.ticker.TickerPriceInfo;
import com.zonbeozon.communityapp.crpyto.domain.ticker.dto.TickerDto;
import com.zonbeozon.communityapp.crpyto.domain.ticker.repository.TickerRepository;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRate;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.exception.ExchangeRateException;
import com.zonbeozon.communityapp.exchangerate.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TickerService {
    private final TickerRepository tickerRepository;
    private final ExchangeRateService exchangeRateService;

    @Transactional
    public void updateTicker(Market market, TickerDto tickerDto) {
        Ticker oldTicker = market.getTicker();
        Ticker newTicker = Ticker.builder()
                .tickerPriceInfoKrw(createTickerPriceInfoKrw(tickerDto))
                .tickerPriceInfoUsd(createTickerPriceInfoUsd(tickerDto))
                .signedChangeRate(tickerDto.signedChangeRate())
                .build();
        //이전 ticker 정보가 없을때
        if(oldTicker == null) {
            tickerRepository.save(newTicker);
            market.setTicker(newTicker);
            return;
        }
        //기존 ticker 정보가 있다면 업데이트
        oldTicker.update(newTicker);
    }

    private TickerPriceInfo createTickerPriceInfoKrw(TickerDto tickerDto) {
        return switch (tickerDto.marketType()) {
            case KRW -> createTickerPriceInfo(tickerDto);
            case USDT -> {
                ExchangeRate usdExchangeRate = exchangeRateService.getLatestExchangeRate(ExchangeRateCode.USD);
                yield TickerPriceInfo.builder()
                        .signedChangePrice(ExchangeRateService.calculateOtherCurrencyToKrw(tickerDto.signedChangePrice(), usdExchangeRate))
                        .accTradePrice(ExchangeRateService.calculateOtherCurrencyToKrw(tickerDto.accTradePrice(), usdExchangeRate))
                        .openingPrice(ExchangeRateService.calculateOtherCurrencyToKrw(tickerDto.openingPrice(), usdExchangeRate))
                        .tradePrice(ExchangeRateService.calculateOtherCurrencyToKrw(tickerDto.tradePrice(), usdExchangeRate))
                        .lowPrice(ExchangeRateService.calculateOtherCurrencyToKrw(tickerDto.lowPrice(), usdExchangeRate))
                        .highPrice(ExchangeRateService.calculateOtherCurrencyToKrw(tickerDto.highPrice(), usdExchangeRate))
                        .build();
            }
            case BTC -> throw new ExchangeRateException(ErrorCode.EMPTY_EXCHANGE_RATE);
        };
    }

    private TickerPriceInfo createTickerPriceInfoUsd(TickerDto tickerDto) {
        return switch (tickerDto.marketType()) {
            case KRW -> {
                ExchangeRate usdExchangeRate = exchangeRateService.getLatestExchangeRate(ExchangeRateCode.USD);
                yield TickerPriceInfo.builder()
                        .highPrice(ExchangeRateService.calculateKrwToOtherCurrency(tickerDto.highPrice(), usdExchangeRate))
                        .lowPrice(ExchangeRateService.calculateKrwToOtherCurrency(tickerDto.lowPrice(), usdExchangeRate))
                        .tradePrice(ExchangeRateService.calculateKrwToOtherCurrency(tickerDto.tradePrice(), usdExchangeRate))
                        .openingPrice(ExchangeRateService.calculateKrwToOtherCurrency(tickerDto.openingPrice(), usdExchangeRate))
                        .accTradePrice(ExchangeRateService.calculateKrwToOtherCurrency(tickerDto.accTradePrice(), usdExchangeRate))
                        .signedChangePrice(ExchangeRateService.calculateKrwToOtherCurrency(tickerDto.signedChangePrice(), usdExchangeRate))
                        .build();
            }
            case USDT -> createTickerPriceInfo(tickerDto);
            case BTC -> throw new ExchangeRateException(ErrorCode.EMPTY_EXCHANGE_RATE);
        };
    }

    private TickerPriceInfo createTickerPriceInfo(TickerDto tickerDto) {
        return TickerPriceInfo.builder()
                .signedChangePrice(tickerDto.signedChangePrice())
                .accTradePrice(tickerDto.accTradePrice())
                .openingPrice(tickerDto.openingPrice())
                .tradePrice(tickerDto.tradePrice())
                .lowPrice(tickerDto.lowPrice())
                .highPrice(tickerDto.highPrice())
                .build();
    }
}
