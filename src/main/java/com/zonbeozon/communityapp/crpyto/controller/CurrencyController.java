package com.zonbeozon.communityapp.crpyto.controller;

import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.MiniCurrenciesResponse;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crypto/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/{currencyId}")
    public ResponseEntity<CurrencyResponse> getSingleCurrency(
            @PathVariable long currencyId,
            @RequestParam("fiat") String fiatType
    ) {
        CurrencyResponse currencyResponse = currencyService.getCurrencyResponse(currencyId, fiatType);
        return ResponseEntity.ok().body(currencyResponse);
    }

    @GetMapping
    public ResponseEntity<MiniCurrenciesResponse> getAllCurrencies(@RequestParam("fiat") String fiatType) {
        MiniCurrenciesResponse miniCurrenciesResponse = currencyService.getAllMiniCurrenciesResponse(fiatType);
        return ResponseEntity.ok().body(miniCurrenciesResponse);
    }
}
