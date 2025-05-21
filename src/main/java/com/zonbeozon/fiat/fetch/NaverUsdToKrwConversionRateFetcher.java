package com.zonbeozon.fiat.fetch;

import com.zonbeozon.common.fetch.FetchException;
import com.zonbeozon.fiat.entity.ConversionRateCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static com.zonbeozon.common.utils.BigDecimalUtils.*;

@Component
@Slf4j
class NaverUsdToKrwConversionRateFetcher implements ConversionRateFetcher {
    private static final String BASE_URL = "https://search.naver.com";
    private static final String RESOURCE_URL = "/p/csearch/content/qapirender.nhn?key=calculator&pkid=141&q=%ED%99%98%EC%9C%A8&where=m&u1=keb&u6=standardUnit&u7=0&u3=USD&u4=KRW&u8=down&u2=1";
    private final RestClient restClient;

    public NaverUsdToKrwConversionRateFetcher(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(BASE_URL).build();
    }

    @Override
    public ConversionRateFetchResult fetch(ConversionRateCode code) {
        NaverConversionRateResponse fetchResult = Optional.ofNullable(restClient.get()
                .uri(RESOURCE_URL)
                .retrieve()
                .body(NaverConversionRateResponse.class)).orElseThrow(() -> new FetchException("fetch 결과가 null 입니다."));
        return toFetchResult(fetchResult);
    }

    private ConversionRateFetchResult toFetchResult(NaverConversionRateResponse naverConversionRateFetchResult) {
        log.debug("value of USD_KRW: {}", naverConversionRateFetchResult.country().get(1).value());
        return new ConversionRateFetchResult(
                ConversionRateCode.USD_KRW,
                StringToBigDecimal(naverConversionRateFetchResult.country().get(1).value().replace("," , ""), FIAT_SCALE)
        );
    }

    @Override
    public boolean isSupportedExchangeRateCode(ConversionRateCode code) {
        return code == ConversionRateCode.USD_KRW;
    }

}
