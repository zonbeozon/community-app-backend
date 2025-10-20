package com.zonbeozon.member.service;

import com.zonbeozon.base.AbstractIntegrationTest;
import com.zonbeozon.image.ImageRepository;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.image.service.ImageDeleter;
import com.zonbeozon.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.Mockito.*;

public class MemberProfileServiceTest extends AbstractIntegrationTest {
    @Autowired
    private MemberProfileService memberProfileService;
    @Autowired
    private DefaultMemberProfileProvider defaultMemberProfileProvider;
    @Autowired
    private ImageRepository imageRepository;
    @MockitoSpyBean
    private ImageDeleter imageDeleter;

    private Member member;
    private Image defaultProfile;

    private Image dummyImage_1, dummyImage_2;


    @BeforeEach
    void setup() {
        member = testMemberService.createAndSave();
        defaultProfile = defaultMemberProfileProvider.getDefaultProfile();
        dummyImage_1 = new Image("http://dummyUrl.com/key1", "key1", member);
        dummyImage_2 = new Image("http://dummyUrl.com/key2", "key2", member);
        imageRepository.save(dummyImage_1);
        imageRepository.save(dummyImage_2);
    }

    @Test
    @DisplayName("맴버 프로필이 공유 자원인 경우 이미지는 삭제되지 않는다")
    void shouldNotDeleteImageWhenUpdatingFromSharedResource() {
        memberProfileService.setAsDefaultProfile(member.getId());
        memberProfileService.updateProfile(member.getId(), dummyImage_1.getId(), false);
        Assertions.assertThat(imageRepository.findById(defaultProfile.getId())).isNotNull();
    }

    @Test
    @DisplayName("맴버 프로필 업데이트시 공유 자원이 아닌 경우 기존 프로필 이미지가 삭제 되어야 한다")
    void deletePreviousImageWhenUpdatingFromNonSharedResource() {
        memberProfileService.updateProfile(member.getId(), dummyImage_1.getId(),  false);
        memberProfileService.updateProfile(member.getId(), dummyImage_2.getId(), false);

        verify(imageDeleter, times(1)).deleteImage(eq(dummyImage_1.getId()));
    }

    @Test
    @DisplayName("맴버 프로필 업데이트시 프로필이 변경되어야 한다")
    void updateProfileChangesMemberProfileToNewImage() {
        memberProfileService.updateProfile(member.getId(), dummyImage_1.getId(), false);
        Assertions.assertThat(member.getProfile()).isNotNull();
        Assertions.assertThat(member.getProfile().getImage()).isEqualTo(dummyImage_1);
    }
}
