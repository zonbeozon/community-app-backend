package com.zonbeozon.channel.service;

import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.TestChannelUpdateRequestBuilder;
import com.zonbeozon.channel.dto.ChannelUpdateRequest;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelProfile;
import com.zonbeozon.channel.repository.ChannelProfileRepository;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.image.TestMockImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ChannelUpdateTest extends AbstractChannelIntegrationTest {
    @Autowired
    private ChannelUpdater channelUpdater;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ChannelProfileRepository channelProfileRepository;

    private Channel channel;
    private Member member;

    @BeforeEach
    void setUp() {
        member = testMemberService.createAndSave();
        channel = testChannelService.createAndSave();
        testChannelService.joinAsOwner(channel, member);
    }

    @Test
    @DisplayName("중복 채널 명이 있다면 예외가 발생한다")
    void throwExceptionWhenDuplicateChannelTitleProvided() {
        //create another channel
        testChannelService.createAndSave("duplicate");

        ChannelUpdateRequest request = new TestChannelUpdateRequestBuilder().withTitle("duplicate").build();
        assertThatThrownBy(() -> channelUpdater.updateChannel(channel.getId(), request))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("채널 프로필 이미지 id가 기존에 없었다면 프로필 이미지를 추가한다.")
    void addProfileImageWhenChannelHasNoExistingProfileImage() {
        Image image = new TestMockImageBuilder(member, "dummy").persist(entityManager);
        testChannelService.setChannelProfile(channel, image);

        ChannelUpdateRequest request = new TestChannelUpdateRequestBuilder().withImageId(image.getId()).build();
        channelUpdater.updateChannel(channel.getId(), request);

        Assertions.assertThat(channel.getProfile()).isNotNull();
        Assertions.assertThat(channel.getProfile().getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    @DisplayName("채널 프로필 이미지 id가 기존에 있었지만 변경되었다면 프로필 이미지를 변경한다.")
    void updateProfileImageWhenChannelHasExistingProfileImage() {
        Image existImage = new TestMockImageBuilder(member, "exist").persist(entityManager);
        testChannelService.setChannelProfile(channel, existImage);

        Image newImage = new TestMockImageBuilder(member, "new").persist(entityManager);

        ChannelUpdateRequest request = new TestChannelUpdateRequestBuilder().withImageId(newImage.getId()).build();
        channelUpdater.updateChannel(channel.getId(), request);

        Assertions.assertThat(channel.getProfile()).isNotNull();
        Assertions.assertThat(channel.getProfile().getImage().getId()).isEqualTo(newImage.getId());
    }

    @Test
    @DisplayName("채널 이미지 id가 null로 되어있다면 기존 프로필 이미지를 삭제하고 프로필 이미지를 null로 변경한다.")
    void d() {
        Image existImage = new TestMockImageBuilder(member, "exist").persist(entityManager);
        ChannelProfile profile = testChannelService.setChannelProfile(channel, existImage);

        ChannelUpdateRequest request = new TestChannelUpdateRequestBuilder().withImageId(null).build();
        channelUpdater.updateChannel(channel.getId(), request);

        Assertions.assertThat(channelProfileRepository.findById(profile.getId())).isEmpty();
        Assertions.assertThat(channel.getProfile()).isNull();
    }
}
