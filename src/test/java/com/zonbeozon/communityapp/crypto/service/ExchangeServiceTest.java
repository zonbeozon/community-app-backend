package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crpyto.domain.exchange.repository.ExchangeRepository;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    private ExchangeRepository mockRepository;
    @InjectMocks
    private ExchangeService exchangeService;
    private ExchangeRequest exchangeRequest;
    private Exchange exchange;

    @BeforeEach
    void setUp() {

        exchangeRequest = ExchangeRequest.builder()
                .description("예시거래소 입니다.")
                .englishName("exampleExchange")
                .koreanName("예시거래소")
                .build();

        exchange = ExampleObjectFactory.createExchange(
                exchangeRequest.englishName(),
                exchangeRequest.koreanName(),
                exchangeRequest.description()
        );
    }

    @Test
    void 거래소가_생성되어야_한다() {
        when(mockRepository.existsByEnglishName(exchangeRequest.englishName()))
                .thenReturn(false);
        when(mockRepository.save(any(Exchange.class))).thenReturn(exchange);
        exchangeService.register(exchangeRequest);

        verify(mockRepository, times(1)).save(any(Exchange.class));
    }

    @Test
    void 거래소는_중복_생성되면_안된다() {
        when(mockRepository.existsByEnglishName(exchangeRequest.englishName())).thenReturn(true);

        Assertions.assertThrows(ExchangeException.class,
                () -> exchangeService.register(exchangeRequest)
        );
    }

    @Test
    void 존재하지_않는_거래소를_검색하면_에러를_리턴한다() {
        when(mockRepository.findByEnglishName(exchange.getEnglishName()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ExchangeException.class,
                () -> exchangeService.findByName(exchange.getEnglishName())
        );
    }

    @Test
    void 존재하는_거래소를_영문_이름으로_검색하면_알맞는_거래소를_반환해야_한다() {
        when(mockRepository.findByEnglishName(exchange.getEnglishName()))
                .thenReturn(Optional.of(exchange));

        Exchange actual = exchangeService.findByName(exchange.getEnglishName());

        Assertions.assertEquals(exchange, actual);
    }
}
