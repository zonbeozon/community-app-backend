package com.zonbeozon.communityapp.crypto.controller;

import com.zonbeozon.communityapp.auth.filter.TokenAuthenticationFilter;
import com.zonbeozon.communityapp.crpyto.controller.CurrencyController;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.exception.FiatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CurrencyController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {
                        WebSecurityConfigurer.class,
                        TokenAuthenticationFilter.class
                })
        },
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class, OAuth2ClientAutoConfiguration.class
        }
)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyService currencyService;

    @Test
    @DisplayName("조회시 fiatType 이 잘못입력되면 BadRequest 응답을 반환한다.")
    void shouldReturnBadRequest_whenFiatTypeIsInvalid() throws Exception {
        long currencyId = 1L;
        String invalidFiatType = "sdsd";
        doThrow(new FiatException(ErrorCode.ILLEGAL_FIAT_TYPE)).when(currencyService)
                .getCurrencyResponse(eq(currencyId), eq(invalidFiatType));

        mockMvc.perform(get("/crypto/currency/{currencyId}", currencyId)
                        .param("fiat", invalidFiatType))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ErrorCode.ILLEGAL_FIAT_TYPE.getMessage()));
    }
}
