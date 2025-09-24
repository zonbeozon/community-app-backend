package com.zonbeozon.image.service;

import com.zonbeozon.global.s3.outbox.Outbox;
import com.zonbeozon.global.s3.outbox.OutboxRepository;
import com.zonbeozon.image.ImageS3Properties;
import com.zonbeozon.image.entity.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ImageDeleter {
    private final ImageDbService imageDbService;
    private final ImageFinder imageFinder;
    private final OutboxRepository outboxRepository;
    private final ImageS3Properties imageS3Properties;

    public void deleteImage(Long imageId) {
        Image image = imageFinder.findByIdElseThrow(imageId);
        outboxRepository.save(new Outbox(imageS3Properties.getBucket(), image.getObjectKey()));
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
        images.forEach(image -> outboxRepository.save(new Outbox(imageS3Properties.getBucket(), image.getObjectKey())));
        imageDbService.deleteImagesInBatch(imageIds);
    }
}