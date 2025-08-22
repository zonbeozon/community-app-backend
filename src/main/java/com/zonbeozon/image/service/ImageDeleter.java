package com.zonbeozon.image.service;

import com.zonbeozon.image.entity.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ImageDeleter {
    private final ImageDbService imageDbService;
    private final ImageFinder imageFinder;
    private final ImageS3AsyncDeleter imageS3AsyncDeleter;

    public void deleteImage(Long imageId) {
        Image image = imageFinder.findByIdElseThrow(imageId);
        imageS3AsyncDeleter.deleteAsync(image);
        imageDbService.deleteImages(image.getId());
    }

    public void deleteImages(List<Long> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) {
            return;
        }
        List<Image> images = imageFinder.findAllByIds(imageIds);
        if (images.isEmpty()) {
            return;
        }
        imageS3AsyncDeleter.deleteAsync(images);
        imageDbService.deleteImagesInBatch(imageIds);
    }
}