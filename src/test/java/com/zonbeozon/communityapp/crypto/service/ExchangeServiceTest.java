package com.zonbeozon.communityapp.crypto.service;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.repository.ExchangeRepository;
import com.zonbeozon.communityapp.crpyto.exception.ExchangeException;
import com.zonbeozon.communityapp.crpyto.service.ExchangeService;
import com.zonbeozon.communityapp.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {
    @Mock
    private ExchangeRepository mockRepository;
    @InjectMocks
    private ExchangeService exchangeService;

    @Test
    @DisplayName("거래소는 중복 생성되면 안된다")
    void shouldThrowExceptionWhenExchangeWithSameEnglishNameExists() {
        when(mockRepository.existsByEnglishName(anyString())).thenReturn(true);

        ExchangeException exchangeException = Assertions.assertThrows(ExchangeException.class,
                () -> exchangeService.addExchange(Exchange.builder().englishName("dummy").build())
        );
        Assertions.assertEquals(ErrorCode.DUPLICATE_EXCHANGE, exchangeException.getErrorCode());
    }
}
