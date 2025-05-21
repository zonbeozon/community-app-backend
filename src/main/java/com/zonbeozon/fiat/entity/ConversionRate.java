package com.zonbeozon.fiat.entity;

import com.zonbeozon.common.entity.BaseTimeEntity;
import com.zonbeozon.common.utils.BigDecimalUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
public class ConversionRate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ConversionRateCode code;

    @NotNull
    @Column(scale = BigDecimalUtils.FIAT_SCALE, precision = BigDecimalUtils.FIAT_PRECISION)
    private BigDecimal value;


    public ConversionRate(ConversionRateCode code, BigDecimal value) {
        this.code = code;
        this.value = value;
    }

    public void updateValue(BigDecimal value) {
        this.value = value;
    }
}
