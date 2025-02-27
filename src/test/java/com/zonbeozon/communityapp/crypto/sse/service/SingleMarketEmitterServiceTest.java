package com.zonbeozon.communityapp.crypto.sse.service;

import com.zonbeozon.communityapp.crpyto.domain.currency.Currency;
import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import com.zonbeozon.communityapp.crpyto.sse.entity.SingleMarketEmitter;
import com.zonbeozon.communityapp.crpyto.sse.repository.SingleMarketEmitterRepository;
import com.zonbeozon.communityapp.crpyto.sse.service.SingleMarketEmitterService;
import com.zonbeozon.communityapp.exchangerate.domain.FiatType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SingleMarketEmitterServiceTest {
    @Mock
    private SingleMarketEmitterRepository singleMarketEmitterRepository;
    @Mock
    private ExchangeService exchangeService;
    @Mock
    private Exchange exchange;
    @Mock
    private Currency currency;
    @Mock
    private CurrencyService currencyService;

    private SingleMarketEmitterService singleMarketEmitterService;

    @BeforeEach
    void setup() {
        singleMarketEmitterService  = new SingleMarketEmitterService(
                singleMarketEmitterRepository,
                exchangeService,
                currencyService,
                100L
        );
    }

    @Test
    @DisplayName("emitter를 만들면 리포지토리에 저장하고 리턴한다")
    void createEmitter_SavesAndReturnsEmitter() {
        when(singleMarketEmitterRepository.save(any(SingleMarketEmitter.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeService.findById(eq(1L))).thenReturn(exchange);
        when(currencyService.findById(eq(1L))).thenReturn(currency);
        SingleMarketEmitter singleMarketEmitter = singleMarketEmitterService.createEmitter(
                1L,
                FiatType.USD.name(),
                1L);

        Assertions.assertEquals(FiatType.USD, singleMarketEmitter.getFiatType());
        Assertions.assertEquals(exchange, singleMarketEmitter.getExchange());
        Assertions.assertEquals(currency, singleMarketEmitter.getCurrency());
    }
}
