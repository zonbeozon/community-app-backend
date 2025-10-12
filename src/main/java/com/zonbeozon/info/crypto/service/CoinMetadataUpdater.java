package com.zonbeozon.info.crypto.service;

import com.zonbeozon.info.crypto.domain.*;
import com.zonbeozon.info.crypto.dto.CoinMetadataDto;
import com.zonbeozon.info.crypto.repository.CoinMetadataRepository;
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
public class CoinMetadataUpdater {
    private final CoinMetadataRepository coinMetadataRepository;
    private final SupportedCoinListProvider supportedCoinListProvider;
    private final CoinMetadataProvider coinMetadataProvider;
    private final CoinInfoDataHealthIndicator coinInfoDataHealthIndicator;

    @Scheduled(cron = "${coin-info.metadata.update-interval-cron}")
    public void update() {
        try {
            Set<String> existSymbols = supportedCoinListProvider.getSupportedCoinSymbols();
            List<CoinMetadata> metadataToUpdate = convertDtoToCoinMetadata(coinMetadataProvider.provide(existSymbols));
            coinMetadataRepository.replaceAll(metadataToUpdate);
            coinInfoDataHealthIndicator.recordMetadataSuccess();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<CoinMetadata> convertDtoToCoinMetadata(List<CoinMetadataDto> metadataDtos) {
        return metadataDtos.stream().map(newMetadata -> {
            Map<LanguageCode, LocalizedCoinInfo> localizedInfoMap = new HashMap<>();
            newMetadata.localizedInfos().forEach((languageCode, localizedCoinInfoDto) -> {
                localizedInfoMap.put(
                        languageCode,
                        new LocalizedCoinInfo(localizedCoinInfoDto.getName(), localizedCoinInfoDto.getDescription())
                );
            });
            return new CoinMetadata(
                    newMetadata.symbol(),
                    newMetadata.logo(),
                    localizedInfoMap,
                    newMetadata.website(),
                    newMetadata.lastUpdated()
            );
        }).toList();
    }
}
