package com.zonbeozon.fiat.repository;

import com.zonbeozon.fiat.entity.ConversionRate;
import com.zonbeozon.fiat.entity.ConversionRateCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
public class ConversionRateRepositoryTest {
    @Autowired
    private ConversionRateRepository repository;

    @Test
    @DisplayName("환율정보를 최신 순으로 가져와야 한다.")
    void givenMultipleRates_whenFindFirstByCodeOrderByCreatedAtDesc_thenReturnLatest() {
        BigDecimal firstInput = BigDecimal.valueOf(1400);
        BigDecimal secondInput = BigDecimal.valueOf(1500);
        repository.save(new ConversionRate(ConversionRateCode.USD_KRW, firstInput));
        repository.save(new ConversionRate(ConversionRateCode.USD_KRW, secondInput));

        Optional<ConversionRate> conversionRate =  repository.findFirstByCodeOrderByCreatedAtDesc(ConversionRateCode.USD_KRW);

        Assertions.assertThat(conversionRate)
                .isPresent()
                .get()
                .extracting(ConversionRate::getValue)
                .isEqualTo(secondInput);
    }
}
