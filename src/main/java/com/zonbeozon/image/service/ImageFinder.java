package com.zonbeozon.image.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.image.ImageRepository;
import com.zonbeozon.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageFinder {
    private final ImageRepository imageRepository;

    public Image findById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new NotFoundException(ErrorCode.IMAGE_NOT_FOUND));
    }

    public List<Image> findAllById(List<Long> imageIds) {
        List<Image> images = imageRepository.findAllById(imageIds);
        if(images.size() != imageIds.size()) {
            throw new NotFoundException(ErrorCode.IMAGE_NOT_FOUND);
        }
        return images;
    }

}
