package com.zonbeozon.communityapp.crpyto.controller.admin;

import com.zonbeozon.communityapp.crpyto.controller.dto.market.AdminMarketResponse;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.AdminMarketResponseWrapper;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketAddRequest;
import com.zonbeozon.communityapp.crpyto.domain.market.MarketStatus;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import com.zonbeozon.communityapp.crpyto.service.market.MarketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/crypto/market")
@RequiredArgsConstructor
public class AdminMarketController {
    private final MarketService marketService;
    private final CurrencyService currencyService;

    @PostMapping
    public ResponseEntity<Void> addSingleMarket(@RequestBody @Valid MarketAddRequest request) {
        currencyService.addMarket(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<AdminMarketResponseWrapper> getEntireMarkets(@RequestParam long currencyId) {
        //currencyId check
        currencyService.findById(currencyId);
        return ResponseEntity.ok(marketService.createAdminMarketResponseWrapper(currencyId));

    }

    @DeleteMapping("{marketId}")
    public ResponseEntity<Void> deleteSingleMarket(@PathVariable long marketId) {
        marketService.deleteMarket(marketId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{marketId}/status")
    public ResponseEntity<MarketStatus> changeMarketStatus(@PathVariable long marketId) {
        return ResponseEntity.ok(marketService.changeMarketStatus(marketId));
    }
}
