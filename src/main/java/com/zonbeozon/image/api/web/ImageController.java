package com.zonbeozon.image.api.web;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.image.api.ImageUploadApi;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.image.validation.ValidImageFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/images")
@Tag(name = "이미지", description = "이미지 관련 엔드포인트")
public class ImageController {
    private final ImageUploadApi imageUploadApi;

    @Operation(
            summary = "이미지 업로드",
            description = """
                    이미지를 업로드하고 해당 이미지에 대한 id를 받는다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDto> uploadImage(
            @ValidImageFile MultipartFile image
    ) throws IOException {
        ImageDto imageDto = imageUploadApi.uploadImage(image.getInputStream(), image.getSize(), image.getContentType());
        return ResponseEntity.status(HttpStatus.CREATED).body(imageDto);
    }
}
