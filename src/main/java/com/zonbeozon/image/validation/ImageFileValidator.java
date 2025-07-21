package com.zonbeozon.image.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageFileValidator implements ConstraintValidator<ValidImageFile, MultipartFile> {
    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE
    );

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("image 필드가 비어있습니다.").addConstraintViolation();
            return false;
        }
        String contentType = multipartFile.getContentType();
        return contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType);
    }
}
