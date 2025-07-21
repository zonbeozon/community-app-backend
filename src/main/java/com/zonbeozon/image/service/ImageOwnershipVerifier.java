package com.zonbeozon.image.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.ImageRepository;
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
    private final ImageRepository imageRepository;
    private final AuthenticationService authenticationService;

    public void verify(List<Long> imageIds) {
        Member member = authenticationService.getCurrentMember();
        List<Image> images = imageRepository.findAllById(imageIds);
        images.forEach(image -> {
            if(!member.equals(image.getUploader())) {
                throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
            }
        });
    }
}
