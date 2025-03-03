package com.zonbeozon.communityapp.crpyto.controller;

import com.zonbeozon.communityapp.crpyto.sse.service.EntireMarketEmitterService;
import com.zonbeozon.communityapp.crpyto.sse.service.SingleMarketEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/crypto/market")
@RequiredArgsConstructor
public class MarketController {
    private final EntireMarketEmitterService entireMarketEmitterService;
    private final SingleMarketEmitterService singleMarketEmitterService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getMarket(
            @RequestParam("fiat") String fiatType,
            @RequestParam("exchangeId") Long exchangeId
    ) {
        return ResponseEntity.ok(entireMarketEmitterService.createEmitter(fiatType, exchangeId).getEmitter());
    }

    @GetMapping(value = "/{currencyId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> getMarket(
            @PathVariable("currencyId") Long currencyId,
            @RequestParam("fiat") String fiatType
    ) {
        return ResponseEntity.ok(singleMarketEmitterService.createEmitter(currencyId, fiatType).getEmitter());
    }
}
