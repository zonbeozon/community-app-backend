package com.zonbeozon.image.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.image.service.ImageUploader;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class ImageUploadApi {
    private final ImageUploader imageUploader;
    private final AuthenticationService authenticationService;
    private final ImageFinder imageFinder;

    public ImageDto uploadImage(InputStream fileContentStream, long contentLength, String contentType) {
        Member member = authenticationService.getCurrentMember();
        Long imageId = imageUploader.uploadImage(member.getId(), fileContentStream, contentLength, contentType);
        Image image = imageFinder.findByIdElseThrow(imageId);
        return new ImageDto(imageId, image.getUrl());
    }
}
