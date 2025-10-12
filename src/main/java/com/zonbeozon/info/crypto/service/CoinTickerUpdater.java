package com.zonbeozon.info.crypto.service;

import com.zonbeozon.info.crypto.domain.BaseAsset;
import com.zonbeozon.info.crypto.domain.CoinQuote;
import com.zonbeozon.info.crypto.domain.CoinTicker;
import com.zonbeozon.info.crypto.dto.CoinTickerDto;
import com.zonbeozon.info.crypto.repository.CoinTickerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinTickerUpdater {
    private final CoinTickerRepository coinTickerRepository;
    private final SupportedCoinListProvider supportedCoinListProvider;
    private final CoinTickerProvider coinTickerProvider;
    private final CoinInfoDataHealthIndicator coinInfoDataHealthIndicator;

    @Scheduled(cron = "${coin-info.ticker.update-interval-cron}")
    public void update() {
        try {
            Set<String> existSymbols = supportedCoinListProvider.getSupportedCoinSymbols();
            List<CoinTicker> tickersToUpdate = convertDtoToCoinTicker(coinTickerProvider.provide(existSymbols));
            coinTickerRepository.replaceAll(tickersToUpdate);
            coinInfoDataHealthIndicator.recordTickerSuccess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<CoinTicker> convertDtoToCoinTicker(List<CoinTickerDto> tickerDtos) {
        return tickerDtos.stream().map(newTicker -> {
            Map<BaseAsset, CoinQuote> quoteMap = new HashMap<>();
            newTicker.getQuotes().forEach((baseAsset, quoteDto) -> {
                quoteMap.put(
                        baseAsset,
                        new CoinQuote(quoteDto.getMarketCap(), quoteDto.getFullyDilutedMarketCap(), quoteDto.getVolume24h())
                );
            });
            return new CoinTicker(
                    newTicker.getSymbol(),
                    newTicker.getCurrencyRank(),
                    newTicker.getCirculatingSupply(),
                    newTicker.getTotalSupply(),
                    quoteMap,
                    newTicker.getLastUpdated()
            );
        }).toList();
    }
}
