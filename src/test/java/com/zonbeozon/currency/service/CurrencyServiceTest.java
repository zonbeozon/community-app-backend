package com.zonbeozon.currency.service;

import com.zonbeozon.currency.entity.Currency;
import com.zonbeozon.currency.loader.CurrencyLoader;
import com.zonbeozon.currency.repository.CurrencyRepository;
import com.zonbeozon.global.SpringBootServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static com.zonbeozon.currency.test.CommonCurrencyRelatedData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest extends SpringBootServiceTest {
    @Autowired
    private CurrencyRepository currencyRepository;
    @MockitoBean
    private CurrencyLoader currencyLoader;
    @Autowired
    private CurrencyService currencyService;

    @Nested
    @DisplayName("Currency 통화 추가")
    class addCurrencies {

        @Test
        @DisplayName("중복되는 Symbol을 가진 Currency는 필터링 되어야 한다")
        void givenDuplicateSymbol_whenAddCurrencies_thenDuplicatesAreFilteredOut() {
            //given
            when(currencyLoader.load()).thenReturn(List.of(ETH_DTO, BTC_DTO));
            currencyRepository.save(ETH_DTO.toEntity());
            //when
            currencyService.addCurrencies();
            //then
            List<Currency> all = currencyRepository.findAll();
            assertThat(all)
                    .extracting(Currency::getSymbol)
                    .containsExactlyInAnyOrder(BTC_SYMBOL, ETH_SYMBOL);
        }
    }
}
