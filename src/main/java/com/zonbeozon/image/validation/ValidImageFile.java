package com.zonbeozon.image.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = ImageFileValidator.class)
public @interface ValidImageFile {
    String message() default "Only PNG and JPEG image formats are allowed.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
