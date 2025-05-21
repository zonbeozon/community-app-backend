package com.zonbeozon.currency.controller;

import com.zonbeozon.currency.service.dto.CurrenciesOverviewResponse;
import com.zonbeozon.currency.service.dto.CurrencyResponse;
import com.zonbeozon.currency.service.CurrencyService;
import com.zonbeozon.fiat.entity.FiatType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/{currencyId}")
    public ResponseEntity<CurrencyResponse> getCurrencyResponse(
            @PathVariable long currencyId,
            @RequestParam("fiat") FiatType fiatType
    ) {
        CurrencyResponse currencyResponse = currencyService.getCurrencyResponse(currencyId, fiatType);
        return ResponseEntity.ok().body(currencyResponse);
    }

    @GetMapping
    public ResponseEntity<CurrenciesOverviewResponse> getCurrenciesOverviewResponse(@RequestParam("fiat") FiatType fiatType) {
        CurrenciesOverviewResponse response  = currencyService.getCurrenciesOverviewResponse(fiatType);
        return ResponseEntity.ok().body(response);
    }
}
