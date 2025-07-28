package com.zonbeozon.channel.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.TestChannelProfileBuilder;
import com.zonbeozon.channel.TestChannelUpdateRequestBuilder;
import com.zonbeozon.channel.dto.ChannelUpdateRequest;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelProfile;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.image.TestImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ChannelUpdateTest {
    @Autowired
    private ChannelUpdater channelUpdater;
    @Autowired
    private EntityManager entityManager;

    private Channel channel;
    private Member member;

    @BeforeEach
    void setUp() {
        member = new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
        channel = new TestChannelBuilder().persist(entityManager);
    }

    @Test
    @DisplayName("채널 Owner가 아니라면 채널 업데이트를 호출시 예외가 발생한다")
    void throwExceptionWhenNonOwnerTriesToUpdateChannel() {
        new TestChannelMemberBuilder(member, channel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
        ChannelUpdateRequest request = new TestChannelUpdateRequestBuilder().build();
        assertThatThrownBy(() -> channelUpdater.updateChannel(channel.getId(), request))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("중복 채널 명이 있다면 예외가 발생한다")
    void throwExceptionWhenDuplicateChannelTitleProvided() {
        new TestChannelBuilder().withTitle("duplicate").persist(entityManager);
        new TestChannelMemberBuilder(member, channel).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);
        ChannelUpdateRequest request = new TestChannelUpdateRequestBuilder().withTitle("duplicate").build();
        assertThatThrownBy(() -> channelUpdater.updateChannel(channel.getId(), request))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("채널 프로필 이미지 id가 기존에 없었다면 프로필 이미지를 추가한다.")
    void addProfileImageWhenChannelHasNoExistingProfileImage() {
        Image image = new TestImageBuilder(member, "134").persist(entityManager);

        new TestChannelMemberBuilder(member, channel).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);
        ChannelUpdateRequest request = new TestChannelUpdateRequestBuilder().withImageId(image.getId()).build();
        channelUpdater.updateChannel(channel.getId(), request);

        Assertions.assertThat(channel.getProfile()).isNotNull();
        Assertions.assertThat(channel.getProfile().getImage().getId()).isEqualTo(image.getId());
    }

    @Test
    @DisplayName("채널 프로필 이미지 id가 기존에 있었지만 변경되었다면 프로필 이미지를 변경한다.")
    void updateProfileImageWhenChannelHasExistingProfileImage() {
        Image existImage = new TestImageBuilder(member, "134").persist(entityManager);
        ChannelProfile channelProfile = new TestChannelProfileBuilder(channel, existImage).persist(entityManager);

        channel.updateChannelProfile(channelProfile);
        Image newImage = new TestImageBuilder(member, "1345").persist(entityManager);
        new TestChannelMemberBuilder(member, channel).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);

        ChannelUpdateRequest request = new TestChannelUpdateRequestBuilder().withImageId(newImage.getId()).build();
        channelUpdater.updateChannel(channel.getId(), request);

        Assertions.assertThat(channel.getProfile()).isNotNull();
        Assertions.assertThat(channel.getProfile().getImage().getId()).isEqualTo(newImage.getId());
    }
}
