package com.zonbeozon.communityapp.crypto.domain.exchange;

import com.zonbeozon.communityapp.crpyto.domain.exchange.Exchange;
import com.zonbeozon.communityapp.crpyto.domain.exchange.dto.ExchangeRequest;
import com.zonbeozon.communityapp.crypto.ExampleObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExchangeTest {
    @Test
    void DTO로_부터_엔터티가_제대로_생성되어야_한다() {
        ExchangeRequest exchangeRequest = ExampleObjectFactory.createExampleExchangeRequest(
                "exampleExchange",
                "예시거래소",
                "예시거래소 입니다."
        );
        Exchange exchange = Exchange.fromDto(exchangeRequest);

        Assertions.assertEquals(exchangeRequest.getEnglishName(), exchange.getEnglishName());
        Assertions.assertEquals(exchangeRequest.getKoreanName(), exchange.getKoreanName());
        Assertions.assertEquals(exchangeRequest.getDescription(), exchange.getDescription());
    }
}
