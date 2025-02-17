package com.zonbeozon.communityapp.exchangerate.fetch;

import com.zonbeozon.communityapp.common.utils.BigDecimalUtils;
import com.zonbeozon.communityapp.crpyto.fetch.DefaultRestFetcher;
import com.zonbeozon.communityapp.exception.ErrorCode;
import com.zonbeozon.communityapp.exchangerate.domain.ExchangeRateCode;
import com.zonbeozon.communityapp.exchangerate.domain.dto.ExchangeRateDto;
import com.zonbeozon.communityapp.exchangerate.exception.ExchangeRateException;
import com.zonbeozon.communityapp.exchangerate.fetch.dto.ExchangeRateFetchResult;
import com.zonbeozon.communityapp.exchangerate.fetch.dto.KoreanEximExchangeRateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class KoreaEximExchangeRateFetcher implements ExchangeRateFetcher {
    private static final String KOREA_EXIM_BASE_URL = "https://www.koreaexim.go.kr";
    private static final String KOREA_EXIM_EXCHANGE_RATE_RESOURCE_URL = "/site/program/financial/exchangeJSON";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DefaultRestFetcher defaultRestFetcher;
    private final String authKey;

    @Autowired
    public KoreaEximExchangeRateFetcher(
            DefaultRestFetcher defaultRestFetcher,
            @Value("${exchange-rate.auth-key}") String authKey
    ) {
        this.defaultRestFetcher = defaultRestFetcher;
        this.authKey = authKey;
    }

    @Override
    public ExchangeRateFetchResult fetch(LocalDate searchDate) {
        List<KoreanEximExchangeRateRequest> exchangeRateRequests = defaultRestFetcher.fetchWithParam(
                KOREA_EXIM_BASE_URL + KOREA_EXIM_EXCHANGE_RATE_RESOURCE_URL,
                createParamMap(authKey, searchDate),
                new ParameterizedTypeReference<>() {}
        );
        if(exchangeRateRequests.isEmpty()) {
            throw new ExchangeRateException(ErrorCode.EXCHANGE_RATE_NOT_AVAILABLE);
        }
        return convert(exchangeRateRequests);
    }

    private ExchangeRateFetchResult convert(List<KoreanEximExchangeRateRequest> exchangeRateRequests) {
        List<ExchangeRateDto> filteredExchangeRates = exchangeRateRequests.stream()
                .filter(exchangeRateRequest -> ExchangeRateCode.isSupported(exchangeRateRequest.code()))
                .map(exchangeRateRequest -> new ExchangeRateDto(
                        ExchangeRateCode.valueOf(exchangeRateRequest.code()),
                        BigDecimalUtils.createBigDecimal(exchangeRateRequest.rate(), BigDecimalUtils.FIAT_SCALE)
                ))
                .toList();
        return new ExchangeRateFetchResult(filteredExchangeRates);
    }

    private static MultiValueMap<String,String> createParamMap(String authKey, LocalDate searchDate) {
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("authkey", authKey);
        params.add("searchdate", searchDate.format(formatter));
        params.add("data", "AP01");
        return params;
    }
}
