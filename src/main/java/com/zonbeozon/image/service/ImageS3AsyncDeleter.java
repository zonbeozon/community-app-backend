package com.zonbeozon.image.service;

import com.zonbeozon.image.S3Properties;
import com.zonbeozon.image.entity.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageS3AsyncDeleter {
    private final S3Properties s3Properties;
    private final S3Client s3Client;

    @Async
    public void deleteAsync(Collection<Image> images) {
        Set<ObjectIdentifier> keysToDelete = images.stream()
                .map(image -> ObjectIdentifier.builder().key(image.getObjectKey()).build())
                .collect(Collectors.toSet());

        DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                .bucket(s3Properties.getBucket())
                .delete(Delete.builder().objects(keysToDelete).build())
                .build();

        try {
            s3Client.deleteObjects(deleteObjectsRequest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug("{} keys deleted from S3 asynchronously.", keysToDelete.size());
    }

    @Async
    public void deleteAsync(Image image) {
        String key = image.getObjectKey();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(key)
                .build();
        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug("1 key deleted from S3 asynchronously.");
    }
}
