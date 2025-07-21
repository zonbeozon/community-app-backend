package com.zonbeozon.image.controller;

import com.zonbeozon.image.service.ImageS3Uploader;
import com.zonbeozon.image.validation.ValidImageFile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "이미지", description = "이미지 관련 엔드포인트")
public class ImageController {
    private final ImageS3Uploader imageS3Uploader;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadImage(
            @ValidImageFile MultipartFile image
    ) throws IOException {
        Long imageId = imageS3Uploader.uploadImage(image.getInputStream(), image.getSize(), image.getContentType());
        return ResponseEntity.status(HttpStatus.CREATED).body(imageId);
    }
}
