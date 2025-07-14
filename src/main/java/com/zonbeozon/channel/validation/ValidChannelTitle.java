package com.zonbeozon.channel.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ChannelTitleValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidChannelTitle {
    String message() default "Invalid channel title";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
