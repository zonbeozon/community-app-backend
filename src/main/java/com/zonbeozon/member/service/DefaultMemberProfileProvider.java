package com.zonbeozon.member.service;

import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageFinder;
import com.zonbeozon.image.service.ImageUploader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
@DependsOn("dataSourceScriptDatabaseInitializer")
@RequiredArgsConstructor
public class DefaultMemberProfileProvider {
    private static final String DEFAULT_PROFILE_KEY = "default-member-profile";
    private final ImageUploader imageUploader;
    private final ImageFinder imageFinder;

    @Value("${system-admin.id}")
    private Long systemAdminId;

    private Image defaultMemberProfile;

    @PostConstruct
    public void uploadDefaultImage() throws IOException {
        Optional<Image> optProfile = imageFinder.findByKey(DEFAULT_PROFILE_KEY);
        ClassPathResource resource = new ClassPathResource("data/default_profile.png");
        if(optProfile.isPresent()) {
            defaultMemberProfile = optProfile.get();
            return;
        }
        if (resource.exists()) {
            try (InputStream inputStream = resource.getInputStream()) {
                long contentLength = resource.contentLength();
                Long registeredImageId = imageUploader.uploadImage(systemAdminId, DEFAULT_PROFILE_KEY, inputStream, contentLength, MediaType.IMAGE_PNG_VALUE);
                defaultMemberProfile = imageFinder.findByIdElseThrow(registeredImageId);
                return;
            }
        }
        throw new IllegalStateException("이미지를 찾을 수 없음");
    }

    public Image getDefaultProfile() {
        return defaultMemberProfile;
    }
}
