package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crpyto.domain.exchange.repository.ExchangeRepository;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ExchangeServiceTest {

    private ExchangeRepository mockRepository;
    private ExchangeService exchangeService;
    private ExchangeRequest exchangeRequest;
    private Exchange exchange;

    @BeforeEach
    void setUp() {
        //ExchangeRepository Mockito setup
        mockRepository = mock(ExchangeRepository.class);

        //ExchangeService setup
        exchangeService = new ExchangeService(mockRepository);

        //example request setup
        exchangeRequest = ExampleObjectFactory.createExampleExchangeRequest(
                "exampleExchange",
                "예시거래소",
                "예시거래소 입니다."
        );

        //example entity setup
        exchange = ExampleObjectFactory.createExampleExchange(
                1L,
                exchangeRequest.getEnglishName(),
                exchangeRequest.getKoreanName(),
                exchangeRequest.getDescription()
        );
    }

    @Test
    void 거래소가_생성되어야_한다() {
        when(mockRepository.existsByEnglishName(exchangeRequest.getEnglishName()))
                .thenReturn(false);
        when(mockRepository.save(any(Exchange.class))).thenReturn(exchange);

        long id = exchangeService.register(exchangeRequest);

        Assertions.assertEquals(exchange.getId(), id);
    }

    @Test
    void 거래소는_중복_생성되면_안된다() {
        when(mockRepository.existsByEnglishName(exchangeRequest.getEnglishName())).thenReturn(true);

        Assertions.assertThrows(RuntimeException.class,
                () -> exchangeService.register(exchangeRequest)
        );
    }

    @Test
    void 존재하지_않는_거래소를_검색하면_에러를_리턴한다() {
        when(mockRepository.findByEnglishName(exchange.getEnglishName()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class,
                () -> exchangeService.findByName(exchange.getEnglishName())
        );
    }

    @Test
    void 존재하는_거래소를_영문_이름으로_검색하면_제대로_반환해야_한다() {
        when(mockRepository.findByEnglishName(exchange.getEnglishName()))
                .thenReturn(Optional.of(exchange));

        Exchange actual = exchangeService.findByName(exchange.getEnglishName());

        Assertions.assertEquals(exchange, actual);
    }
}
