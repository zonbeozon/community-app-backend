package com.zonbeozon.image.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageOwnershipVerifier {
    private final AuthenticationService authenticationService;
    private final ImageFinder imageFinder;

    public void verify(List<Long> imageIds) {
        Member member = authenticationService.getCurrentMember();
        List<Image> images = imageFinder.findAllByIds(imageIds);
        images.forEach(image -> {
            if(!member.equals(image.getUploader())) {
                throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
            }
        });
    }

    public void verify(Long imageId) {
        Member member = authenticationService.getCurrentMember();
        Image image = imageFinder.findByIdElseThrow(imageId);
        if(!member.equals(image.getUploader())) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }
}
