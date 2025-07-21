package com.zonbeozon.image.service;

import com.zonbeozon.image.ImageRepository;
import com.zonbeozon.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageDbUploader {
    private final ImageRepository imageRepository;

    public Long saveImage(Image image) {
        return imageRepository.save(image).getId();
    }
}
