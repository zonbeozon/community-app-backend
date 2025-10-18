package com.zonbeozon.image;

import com.zonbeozon.base.AbstractIntegrationTest;
import com.zonbeozon.global.UUIDGenerator;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageUploader;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ImageUploadTest extends AbstractIntegrationTest {
    @Autowired
    private ImageUploader imageUploader;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private ImageS3Properties imageS3Properties;
    @MockitoBean
    private UUIDGenerator uuidGenerator;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private EntityManager entityManager;

    private static final String uuid = UUID.randomUUID().toString();

    private static long contentLength;
    private static File imageFile;
    private InputStream is;
    private Member member;

    @BeforeAll
    static void setupImage() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("data/test-image.jpeg");
        imageFile = classPathResource.getFile();
        contentLength = imageFile.length();
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.when(uuidGenerator.generateUUID()).thenReturn(uuid);
        is = new FileInputStream(imageFile);
        member = testMemberService.createAndSave();
    }

    @DisplayName("이미지 업로드 성공시 key와 uri가 저장되어야 한다")
    @Test
    void uploadImageShouldSaveKeyAndUriToDatabase() {
        Long imageId = imageUploader.uploadImage(member.getId(), is, contentLength ,  "image/jpeg");
        Image image = imageRepository.findById(imageId).get();
        Assertions.assertThat(image.getUrl()).isEqualTo(imageS3Properties.getEndpoint() + "/" + uuid);
        Assertions.assertThat(image.getObjectKey()).isEqualTo(uuid);
        Assertions.assertThat(image.getUploader().getId()).isEqualTo(member.getId());
    }
}
