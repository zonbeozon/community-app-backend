package com.zonbeozon.info.crypto.api.web;

import com.zonbeozon.info.crypto.api.CoinInfoQueryApi;
import com.zonbeozon.info.crypto.domain.BaseAsset;
import com.zonbeozon.info.crypto.domain.LanguageCode;
import com.zonbeozon.info.crypto.dto.CoinInfoDto;
import com.zonbeozon.info.crypto.dto.SimplifiedCoinInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info/coin")
public class CoinInfoController {
    private final CoinInfoQueryApi coinInfoQueryApi;

    @GetMapping("/{symbol}")
    public CoinInfoDto getCoinInfo(
            @PathVariable("symbol") String symbol,
            @RequestParam(defaultValue = "EN") LanguageCode languageCode,
            @RequestParam(defaultValue = "USD") BaseAsset baseAsset
            ) {
        return coinInfoQueryApi.getCoinInfo(symbol, languageCode, baseAsset);
    }

    @GetMapping
    public List<SimplifiedCoinInfoDto> getSimplifiedCoinInfos(
            @RequestParam(defaultValue = "EN") LanguageCode languageCode
    ) {
        return coinInfoQueryApi.getSimplifiedCoinInfosOrderByRank(languageCode);
    }
}
