package com.zonbeozon.communityapp.crypto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonbeozon.communityapp.auth.filter.TokenAuthenticationFilter;
import com.zonbeozon.communityapp.crpyto.controller.admin.AdminCurrencyController;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.CurrencyRequest;
import com.zonbeozon.communityapp.crpyto.controller.dto.currency.DescriptionUpdateRequest;
import com.zonbeozon.communityapp.crpyto.controller.dto.market.MarketRequest;
import com.zonbeozon.communityapp.crpyto.exception.CurrencyException;
import com.zonbeozon.communityapp.crpyto.service.currency.CurrencyService;
import com.zonbeozon.communityapp.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AdminCurrencyController.class,
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
class AdminCurrencyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyService currencyService;

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화를 위해 추가

    @Test
    @DisplayName("올바른 requestBody로 addCurrency 호출 시 201 Created 응답과 Location 헤더를 반환한다.")
    void shouldReturnCreatedWithLocationHeader_whenAddCurrencyWithValidRequest() throws Exception {
        CurrencyRequest request = new CurrencyRequest(
                "BTC",
                "비트코인",
                List.of(new MarketRequest("ex_1", List.of("krw")))) ;
        String requestJson = objectMapper.writeValueAsString(request);

        when(currencyService.addCurrency(any(CurrencyRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/admin/crypto/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","/crypto/currency/1"));

        verify(currencyService).addCurrency(any(CurrencyRequest.class));
    }

    @Test
    @DisplayName("입력값이 validation에서 잘못되었다면 400 Bad Request 응답을 반환한다.")
    void shouldReturnBadRequest_whenAddCurrencyWithInvalidRequest() throws Exception {
        CurrencyRequest request = new CurrencyRequest(
                "",
                "비트코인",
                List.of(new MarketRequest("ex_1", List.of("krw")))) ;
        String requestJson = objectMapper.writeValueAsString(request);

        when(currencyService.addCurrency(any(CurrencyRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/admin/crypto/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("올바른 currencyId를 전달하면 삭제에 성공하고 204 No Content 응답을 반환한다.")
    void shouldReturnNoContent_whenDeleteCurrencyWithValidId() throws Exception {
        // Given
        long currencyId = 1L;
        doNothing().when(currencyService).deleteCurrency(currencyId);

        // When & Then
        mockMvc.perform(delete("/admin/crypto/currency/{currencyId}", currencyId))
                .andExpect(status().isNoContent());

        verify(currencyService).deleteCurrency(currencyId);
    }

    @Test
    @DisplayName("존재하지 않는 currencyId를 전달하면 404 Not Found 응답을 반환한다.")
    void shouldReturnNotFound_whenDeleteCurrencyWithInvalidId() throws Exception {
        // Given
        Long currencyId = 1L;
        doThrow(new CurrencyException(ErrorCode.CURRENCY_NOT_FOUND)).when(currencyService).deleteCurrency(eq(currencyId));

        // When & Then
        mockMvc.perform(delete("/admin/crypto/currency/{currencyId}", currencyId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ErrorCode.CURRENCY_NOT_FOUND.getMessage()));
    }

    @Test
    @DisplayName("한글 디스크립션 업데이트 시 성공하면 204 No Content 응답을 반환한다.")
    void shouldReturnNoContent_whenUpdateKoreanDescriptionSuccessfully() throws Exception {
        // Given
        Long currencyId = 1L;
        DescriptionUpdateRequest request = new DescriptionUpdateRequest("비트코인 설명");
        String requestJson = objectMapper.writeValueAsString(request);

        doNothing().when(currencyService).updateKoreanDescription(eq(currencyId), eq("비트코인 설명"));

        // When & Then
        mockMvc.perform(patch("/admin/crypto/currency/{currencyId}/koreanDescription", currencyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNoContent());

        verify(currencyService).updateKoreanDescription(currencyId, "비트코인 설명");
    }
}