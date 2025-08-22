package com.zonbeozon.image.service;

import com.zonbeozon.image.ImageRepository;
import com.zonbeozon.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageDbService {
    private final ImageRepository imageRepository;

    public Long saveImage(Image image) {
        return imageRepository.save(image).getId();
    }

    public void deleteImages(Long id) {
        imageRepository.deleteById(id);
    }

    public void deleteImagesInBatch(List<Long> imageIds) {
        imageRepository.deleteImagesByIdsInBatch(imageIds);
    }
}
