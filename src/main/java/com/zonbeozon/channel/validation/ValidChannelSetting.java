package com.zonbeozon.channel.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ChannelSettingValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidChannelSetting {
    String message() default "Invalid channel setting";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
