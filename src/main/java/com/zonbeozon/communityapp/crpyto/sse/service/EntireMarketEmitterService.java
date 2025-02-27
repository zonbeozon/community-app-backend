package com.zonbeozon.communityapp.crpyto.sse.service;

import com.zonbeozon.communityapp.crpyto.controller.dto.market.EntireMarketInfoResponse;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.sse.entity.EntireMarketEmitter;
import com.zonbeozon.communityapp.crpyto.sse.repository.EntireMarketEmitterRepository;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
public class EntireMarketEmitterService {
    private final EntireMarketEmitterRepository entireMarketEmitterRepository;
    private final ExchangeService exchangeService;
    private final Long timeout;

    @Autowired
    public EntireMarketEmitterService(
            EntireMarketEmitterRepository entireMarketEmitterRepository,
            ExchangeService exchangeService,
            @Value("${sse.timeout}") Long timeout
    ) {
        this.entireMarketEmitterRepository = entireMarketEmitterRepository;
        this.exchangeService = exchangeService;
        this.timeout = timeout;
    }

    public EntireMarketEmitter createEmitter(String fiatType, Long exchangeId) {
        EntireMarketEmitter entireMarketEmitter = entireMarketEmitterRepository.save(
                EntireMarketEmitter.builder()
                    .emitter(new SseEmitter(timeout))
                    .fiatType(FiatType.parse(fiatType))
                    .exchange(exchangeService.findById(exchangeId))
                    .build()
        );

        entireMarketEmitter.onTimeout(() -> entireMarketEmitter.complete());
        entireMarketEmitter.onError((e) -> entireMarketEmitter.complete());
        entireMarketEmitter.onCompletion(() -> entireMarketEmitterRepository.delete(entireMarketEmitter));

        return entireMarketEmitter;
    }

    public void sendToAll(Map<Exchange, Map<FiatType, EntireMarketInfoResponse>> map) {
        entireMarketEmitterRepository.findAll().forEach(emitter ->
            send(map.get(emitter.getExchange()).get(emitter.getFiatType()), emitter)
        );
    }

    private void send(Object data, EntireMarketEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("entireMarketInfo").data(data, MediaType.APPLICATION_JSON));
        } catch (IOException e) {
            entireMarketEmitterRepository.delete(emitter);
        }
    }
}
