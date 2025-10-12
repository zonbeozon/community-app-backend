package com.zonbeozon.info.crypto.service;

import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.info.crypto.domain.BaseAsset;
import com.zonbeozon.info.crypto.domain.CoinMetadata;
import com.zonbeozon.info.crypto.domain.CoinTicker;
import com.zonbeozon.info.crypto.domain.LanguageCode;
import com.zonbeozon.info.crypto.dto.CoinInfoDto;
import com.zonbeozon.info.crypto.dto.CoinQuoteDto;
import com.zonbeozon.info.crypto.dto.LocalizedCoinInfoDto;
import com.zonbeozon.info.crypto.dto.SimplifiedCoinInfoDto;
import com.zonbeozon.info.crypto.repository.CoinMetadataRepository;
import com.zonbeozon.info.crypto.repository.CoinTickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoinInfoQueryService {
    private final CoinTickerRepository coinTickerRepository;
    private final CoinMetadataRepository coinMetadataRepository;

    public CoinInfoDto getCoinInfo(String symbol, LanguageCode languageCode, BaseAsset baseAsset) {
        CoinMetadata coinMetadata = coinMetadataRepository.findBySymbol(symbol)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SYMBOL_NOT_FOUND));
        if(!coinMetadata.getLocalizedInfos().containsKey(languageCode)) throw new BadRequestException(ErrorCode.SYMBOL_NOT_FOUND);
        CoinTicker coinTicker = coinTickerRepository.findBySymbol(symbol)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SYMBOL_NOT_FOUND));
        if(!coinTicker.getQuotes().containsKey(baseAsset)) throw new BadRequestException(ErrorCode.UNSUPPORTED_BASE_ASSET);

        return new CoinInfoDto(
                symbol,
                coinMetadata.getLogo(),
                LocalizedCoinInfoDto.from(coinMetadata.getLocalizedInfos().get(languageCode)),
                coinMetadata.getWebsite(),
                coinMetadata.getLastUpdated(),
                coinTicker.getCurrencyRank(),
                coinTicker.getCirculatingSupply(),
                coinTicker.getTotalSupply(),
                CoinQuoteDto.from(coinTicker.getQuotes().get(baseAsset)),
                coinTicker.getLastUpdated()
        );
    }

    public List<SimplifiedCoinInfoDto> getSimplifiedCoinInfosOrderByRank(LanguageCode languageCode) {
        List<CoinMetadata> metadataList = coinMetadataRepository.findAll();
        if(metadataList.isEmpty() && !metadataList.getFirst().getLocalizedInfos().containsKey(languageCode)) throw new BadRequestException(ErrorCode.SYMBOL_NOT_FOUND);
        Map<String, CoinTicker> tickerMap = coinTickerRepository.findAllAsMap();
        verifyMissingSymbols(metadataList, tickerMap);
        return metadataList.stream()
                .map(metadata -> {
                    CoinTicker ticker = tickerMap.get(metadata.getSymbol());
                    return new SimplifiedCoinInfoDto(
                            metadata.getSymbol(),
                            metadata.getLogo(),
                            metadata.getLocalizedInfos().get(languageCode).getName(),
                            ticker.getCurrencyRank()
                    );
                })
                .sorted(Comparator.comparing(SimplifiedCoinInfoDto::rank))
                .toList();
    }

    private void verifyMissingSymbols(List<CoinMetadata> metadataList, Map<String, CoinTicker> tickerMap) {
        List<String> missingSymbols = metadataList.stream()
                .map(CoinMetadata::getSymbol)
                .filter(symbol -> !tickerMap.containsKey(symbol))
                .toList();

        if (!missingSymbols.isEmpty()) {
            throw new IllegalStateException(
                    "데이터 불일치: 해당 심볼에 대한 Ticker 정보가 없습니다. " + missingSymbols
            );
        }
    }


}
