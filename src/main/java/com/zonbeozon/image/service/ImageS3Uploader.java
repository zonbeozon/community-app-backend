package com.zonbeozon.image.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.UUIDGenerator;
import com.zonbeozon.image.S3Properties;
import com.zonbeozon.image.S3UriBuilder;
import com.zonbeozon.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImageS3Uploader {
    private final S3Client s3Client;
    private final S3Properties s3Properties;
    private final ImageDbUploader imageDbUploader;
    private final AuthenticationService authenticationService;
    private final UUIDGenerator uuidGenerator;
    private final S3UriBuilder s3UriBuilder;

    public Long uploadImage(InputStream fileContentStream, long contentLength, String contentType) {
        String key = uuidGenerator.generateUUID();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(key)
                .contentType(contentType)
                .contentLength(contentLength)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileContentStream, contentLength));
        Image image = new Image(
                s3UriBuilder.build(key),
                key,
                authenticationService.getCurrentMember()
        );
        return imageDbUploader.saveImage(image);
    }
}
