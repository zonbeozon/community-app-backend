package com.zonbeozon.image.service;

import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageOwnershipVerifier {
    private final ImageFinder imageFinder;

    public void verify(Long memberId, List<Long> imageIds) {
        if(imageIds.isEmpty()) return;
        List<Image> images = imageFinder.findAllByIds(imageIds);
        images.forEach(image -> {
            if(!memberId.equals(image.getUploader().getId())) {
                throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
            }
        });
    }

    public void verify(Long memberId, Long imageId) {
        if(imageId == null) return;
        Image image = imageFinder.findByIdElseThrow(imageId);
        if(!memberId.equals(image.getUploader().getId())) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }
}
