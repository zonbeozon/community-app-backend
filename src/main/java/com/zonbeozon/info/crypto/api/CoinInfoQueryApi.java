package com.zonbeozon.info.crypto.api;

import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.info.crypto.domain.BaseAsset;
import com.zonbeozon.info.crypto.domain.LanguageCode;
import com.zonbeozon.info.crypto.dto.CoinInfoDto;
import com.zonbeozon.info.crypto.dto.SimplifiedCoinInfoDto;
import com.zonbeozon.info.crypto.service.CoinInfoDataHealthIndicator;
import com.zonbeozon.info.crypto.service.CoinInfoQueryService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApiComponent
@RequiredArgsConstructor
public class CoinInfoQueryApi {
    private final CoinInfoQueryService coinInfoQueryService;
    private final CoinInfoDataHealthIndicator coinInfoDataHealthIndicator;

    public CoinInfoDto getCoinInfo(String symbol, LanguageCode languageCode, BaseAsset baseAsset) {
        coinInfoDataHealthIndicator.checkDataHealth();
        return coinInfoQueryService.getCoinInfo(symbol, languageCode, baseAsset);
    }

    public List<SimplifiedCoinInfoDto> getSimplifiedCoinInfosOrderByRank(LanguageCode languageCode) {
        coinInfoDataHealthIndicator.checkDataHealth();
        return coinInfoQueryService.getSimplifiedCoinInfosOrderByRank(languageCode);
    }
}
