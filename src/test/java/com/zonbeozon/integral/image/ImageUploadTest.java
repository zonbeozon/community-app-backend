package com.zonbeozon.integral.image;

import com.zonbeozon.global.UUIDGenerator;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.ImageRepository;
import com.zonbeozon.image.service.ImageS3Uploader;
import com.zonbeozon.image.S3Properties;
import com.zonbeozon.member.TestMemberBuilder;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@SpringBootTest
@Transactional
public class ImageUploadTest {
    @Autowired
    private ImageS3Uploader imageS3Uploader;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3Properties s3Properties;
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

    @BeforeAll
    static void setupImage() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/test-image.jpeg");
        imageFile = classPathResource.getFile();
        contentLength = imageFile.length();
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.when(uuidGenerator.generateUUID()).thenReturn(uuid);
        new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
        is = new FileInputStream(imageFile);
    }

    @DisplayName("이미지 업로드 성공시 key와 uri가 저장되어야 한다")
    @Test
    void uploadImageShouldSaveKeyAndUriToDatabase() {
        Long imageId = imageS3Uploader.uploadImage(is, contentLength ,  "image/jpeg");

        Image image = imageRepository.findById(imageId).get();
        Assertions.assertThat(image.getUrl()).isEqualTo(s3Properties.getEndpoint() + "/" + uuid);
        Assertions.assertThat(image.getObjectKey()).isEqualTo(uuid);
        Assertions.assertThat(image.getUploader().getUsername()).isEqualTo("choi");
    }

    @DisplayName("이미지 업로드 성공시 s3Mock에 이미지가 저장되어야한다")
    @Test
    void uploadImageShouldStoreFileInS3Mock() {
        Long imageId = imageS3Uploader.uploadImage(is, contentLength, "image/jpeg");
        HeadObjectResponse headResponse = s3Client.headObject(HeadObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(uuid)
                .build());

        Assertions.assertThat(headResponse).isNotNull();
    }
}
