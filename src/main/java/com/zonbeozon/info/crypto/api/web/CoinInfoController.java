package com.zonbeozon.info.crypto.api.web;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.info.crypto.api.CoinInfoQueryApi;
import com.zonbeozon.info.crypto.domain.BaseAsset;
import com.zonbeozon.info.crypto.domain.LanguageCode;
import com.zonbeozon.info.crypto.dto.CoinInfoDto;
import com.zonbeozon.info.crypto.dto.SimplifiedCoinInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/info/coin")
@Tag(name = "코인 관련 데이터", description = "코인 관련 데이터 엔드포인트")
public class CoinInfoController {
    private final CoinInfoQueryApi coinInfoQueryApi;

    @Operation(
            summary = "심볼 기반 조회",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CoinInfoDto.class))
            )
    )
    @GetMapping("/{symbol}")
    public ResponseEntity<CoinInfoDto> getCoinInfo(
            @PathVariable("symbol") String symbol,
            @RequestParam(defaultValue = "EN") LanguageCode languageCode,
            @RequestParam(defaultValue = "USD") BaseAsset baseAsset
    ) {
        return ResponseEntity.ok(coinInfoQueryApi.getCoinInfo(symbol, languageCode, baseAsset));
    }

    @Operation(
            summary = "지원 리스트 조회",
            description = "지원되는 모든 코인 리스트가 반환된다. 반환 순서는 Rank 순이다.",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = SimplifiedCoinInfoDto.class))
            )
    )
    @GetMapping
    public List<SimplifiedCoinInfoDto> getSimplifiedCoinInfos(
            @RequestParam(defaultValue = "EN") LanguageCode languageCode
    ) {
        return coinInfoQueryApi.getSimplifiedCoinInfosOrderByRank(languageCode);
    }
}
