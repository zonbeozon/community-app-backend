package com.zonbeozon.image.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.annotation.ApiComponent;
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

    public Long uploadImage(InputStream fileContentStream, long contentLength, String contentType) {
        Member member = authenticationService.getCurrentMember();
        return imageUploader.uploadImage(member.getId(), fileContentStream, contentLength, contentType);
    }
}
