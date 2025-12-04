package com.zonbeozon.info.coin;

import com.zonbeozon.info.crypto.domain.CoinMetadata;
import com.zonbeozon.info.crypto.repository.CoinMetadataRepository;
import com.zonbeozon.info.crypto.service.CoinInfoDataHealthIndicator;
import com.zonbeozon.info.crypto.service.CoinMetadataUpdater;
import com.zonbeozon.info.crypto.service.SupportedCoinListProvider;
import com.zonbeozon.test.AbstractIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;
import java.util.Set;

public class MetadataUpdaterTest extends AbstractIntegrationTest {
    @Autowired
    private CoinMetadataUpdater metadataUpdater;
    @Autowired
    private CoinInfoDataHealthIndicator healthIndicator;
    @Autowired
    private CoinMetadataRepository coinMetadataRepository;
    @MockitoSpyBean
    private SupportedCoinListProvider supportedCoinListProvider;

    @Test
    @DisplayName("")
    void dd() {
        Mockito.when(supportedCoinListProvider.getSupportedCoinSymbols()).thenReturn(Set.of("ETH", "BTC"));
        metadataUpdater.update();
        healthIndicator.checkDataHealth();
        List<CoinMetadata> coinMetadata = coinMetadataRepository.findAll();
        Assertions.assertThat(coinMetadata).hasSize(2);
        Assertions.assertThat(coinMetadata).extracting(CoinMetadata::getSymbol).contains("ETH", "BTC");
    }
}
