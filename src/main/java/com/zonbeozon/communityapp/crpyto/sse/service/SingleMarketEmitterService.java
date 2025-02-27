package com.zonbeozon.communityapp.crpyto.sse.service;

import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import com.zonbeozon.communityapp.crpyto.sse.entity.EntireMarketEmitter;
import com.zonbeozon.communityapp.crpyto.sse.entity.SingleMarketEmitter;
import com.zonbeozon.communityapp.crpyto.sse.repository.SingleMarketEmitterRepository;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
public class SingleMarketEmitterService {
    private final SingleMarketEmitterRepository singleMarketEmitterRepository;
    private final ExchangeService exchangeService;
    private final CurrencyService currencyService;
    private final Long timeout;

    public SingleMarketEmitterService(
            SingleMarketEmitterRepository singleMarketEmitterRepository,
            ExchangeService exchangeService,
            CurrencyService currencyService,
            @Value("${sse.timeout}") Long timeout
    ) {
        this.singleMarketEmitterRepository = singleMarketEmitterRepository;
        this.exchangeService = exchangeService;
        this.currencyService = currencyService;
        this.timeout = timeout;

    }

    public SingleMarketEmitter createEmitter(
            Long currencyId,
            String fiatType,
            Long exchangeId
    ) {
        SingleMarketEmitter singleMarketEmitter = singleMarketEmitterRepository.save(
                SingleMarketEmitter.builder()
                        .emitter(new SseEmitter(timeout))
                        .fiatType(FiatType.parse(fiatType))
                        .exchange(exchangeService.findById(exchangeId))
                        .currency(currencyService.findById(currencyId))
                        .build()
        );

        singleMarketEmitter.onTimeout(() -> singleMarketEmitter.complete());
        singleMarketEmitter.onError((e) -> singleMarketEmitter.complete());
        singleMarketEmitter.onCompletion(() -> singleMarketEmitterRepository.delete(singleMarketEmitter));

        return singleMarketEmitter;
    }

    public void sendToAll(Map<Currency, Map<FiatType, MarketInfoResponse>> map) {
        singleMarketEmitterRepository.findAll().forEach(emitter ->
                send(map.get(emitter.getCurrency()).get(emitter.getFiatType()), emitter)
        );
    }

    private void send(Object data, SingleMarketEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("singleMarketInfo").data(data, MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            singleMarketEmitterRepository.delete(emitter);
        }
    }
}
