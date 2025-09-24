package com.zonbeozon.image.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.UUIDGenerator;
import com.zonbeozon.image.ImageS3Properties;
import com.zonbeozon.image.S3UriBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImageUploader {
    private final S3Client s3Client;
    private final ImageS3Properties imageS3Properties;
    private final ImageDbService imageDbService;
    private final UUIDGenerator uuidGenerator;
    private final S3UriBuilder s3UriBuilder;
    private final MemberFinder memberFinder;

    public Long uploadImage(Long memberId, InputStream fileContentStream, long contentLength, String contentType) {
        String key = uuidGenerator.generateUUID();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(imageS3Properties.getBucket())
                .key(key)
                .contentType(contentType)
                .contentLength(contentLength)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileContentStream, contentLength));
        Image image = new Image(
                s3UriBuilder.build(key),
                key,
                memberFinder.findByIdElseThrow(memberId)
        );
        return imageDbService.saveImage(image);
    }
}
