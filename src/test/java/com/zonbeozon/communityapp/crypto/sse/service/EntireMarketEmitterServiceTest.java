package com.zonbeozon.communityapp.crypto.sse.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.sse.entity.EntireMarketEmitter;
import com.zonbeozon.communityapp.crpyto.sse.repository.EntireMarketEmitterRepository;
import com.zonbeozon.communityapp.crpyto.sse.service.EntireMarketEmitterService;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntireMarketEmitterServiceTest {
    @Mock
    private EntireMarketEmitterRepository entireMarketEmitterRepository;
    @Mock
    private ExchangeService exchangeService;
    @Mock
    private Exchange exchange;

    private EntireMarketEmitterService entireMarketEmitterService;

    @BeforeEach
    void setup() {
        entireMarketEmitterService = new EntireMarketEmitterService(
                entireMarketEmitterRepository,
                exchangeService,
                100L
        );
    }
    @Test
    @DisplayName("emitter를 만들면 리포지토리에 저장하고 리턴한다")
    void createEmitter_SavesAndReturnsEmitter() {
        when(entireMarketEmitterRepository.save(any(EntireMarketEmitter.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeService.findById(1L)).thenReturn(exchange);
        EntireMarketEmitter entireMarketEmitter = entireMarketEmitterService.createEmitter(FiatType.USD.name(), 1L);

        Assertions.assertEquals(FiatType.USD, entireMarketEmitter.getFiatType());
        Assertions.assertEquals(exchange, entireMarketEmitter.getExchange());
    }
}
