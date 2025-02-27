package com.zonbeozon.communityapp.crpyto.controller.admin;

import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyRequest;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.DescriptionUpdateRequest;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/crypto/currency")
public class AdminCurrencyController {
    private final CurrencyService currencyService;

    @PostMapping
    public ResponseEntity<Void> addCurrency(@RequestBody @Valid CurrencyRequest currencyRequest) {
        currencyService.addCurrency(currencyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{currencyId}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long currencyId) {
        currencyService.deleteCurrency(currencyId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{currencyId}/koreanDescription")
    public ResponseEntity<Void> updateCurrencyKoreanDescription(
            @PathVariable Long currencyId,
            @Valid @RequestBody DescriptionUpdateRequest descriptionUpdateRequest
    ) {
        currencyService.updateKoreanDescription(currencyId, descriptionUpdateRequest.description());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{currencyId}/englishDescription")
    public ResponseEntity<Void> updateCurrencyEnglishDescription(
            @PathVariable Long currencyId,
            @Valid @RequestBody DescriptionUpdateRequest descriptionUpdateRequest
    ) {
        currencyService.updateEnglishDescription(currencyId, descriptionUpdateRequest.description());
        return ResponseEntity.noContent().build();
    }

}
