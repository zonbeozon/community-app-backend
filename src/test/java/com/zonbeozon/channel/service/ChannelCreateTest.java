package com.zonbeozon.channel.service;

import com.zonbeozon.channel.TestChannelCreateRequestBuilder;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.dto.ChannelCreateCommand;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.channel.service.finder.ChannelMemberFinder;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.image.TestMockImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class ChannelCreateTest extends AbstractChannelIntegrationTest {
    @Autowired
    private ChannelCreator channelCreator;
    @Autowired
    private ChannelMemberFinder channelMemberFinder;
    @Autowired
    private ChannelFinder channelFinder;
    @Autowired
    private EntityManager entityManager;

    private Member member;

    @BeforeEach
    void setup() {
        member = testMemberService.createAndSave();
    }

    @Test
    @DisplayName("command로 부터 정상적으로 채널이 저장되어야 한다.")
    void createsChannelSuccessfullyFromCommand() {
        ChannelCreateCommand command = new TestChannelCreateRequestBuilder().build().toCommand();
        Long id = channelCreator.addChannel(member.getId(), command);
        Channel channel = channelFinder.findByIdElseThrow(id);
        assertChannelMetadataEquals(channel, command);
    }

    @Test
    @DisplayName("채널이 생성될때 요청자는 Owner로 등록된다.")
    void registerRequesterAsOwnerWhenChannelIsCreated() {
        ChannelCreateCommand command = new TestChannelCreateRequestBuilder().build().toCommand();
        Long id = channelCreator.addChannel(member.getId(), command);
        Channel channel = channelFinder.findByIdElseThrow(id);
        ChannelMember channelMember = channelMemberFinder.findByChannelIdAndMemberIdElseThrow(channel.getId(), member.getId());
        assertThat(channelMember.getMember()).isEqualTo(member);
        assertThat(channelMember.isOwner()).isTrue();
    }

    @Test
    @DisplayName("중복 채널명은 예외를 발생시킨다.")
    void throwsExceptionWhenCreatingChannelWithDuplicateTitle() {
        ChannelCreateCommand command = new TestChannelCreateRequestBuilder().build().toCommand();
        //first time create
        channelCreator.addChannel(member.getId(), command);

        //second time create with same title
        assertThatThrownBy(()-> channelCreator.addChannel(member.getId(), command))
                .isInstanceOf(ConflictException.class)
                .satisfies(e -> {
                    ConflictException exception = (ConflictException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_CHANNEL_TITLE.name());
                });
    }

    @Test
    @DisplayName("이미지 id가 포함되어 있다면 채널 프로필로 등록한다.")
    void RegisterProfileWhenChannelIsCreatedWithImageId() {
        Image image = new TestMockImageBuilder(member, "1234").persist(entityManager);
        ChannelCreateCommand command = new TestChannelCreateRequestBuilder().setImageId(image.getId()).build().toCommand();
        Long id = channelCreator.addChannel(member.getId(), command);
        Channel channel = channelFinder.findByIdElseThrow(id);

        Assertions.assertThat(channel.getProfile()).isNotNull();
        Assertions.assertThat(channel.getProfile().getImage().getId()).isEqualTo(image.getId());
    }

    static void assertChannelMetadataEquals(Channel channel, ChannelCreateCommand command) {
        assertThat(channel.getTitle()).isEqualTo(command.title());
        assertThat(channel.getDescription()).isEqualTo(command.description());
        assertThat(channel.getSetting().getContentVisibility()).isEqualTo(command.visibility());
        assertThat(channel.getSetting().getJoinPolicy()).isEqualTo(command.joinPolicy());

        if(command.imageId() != null) {
            assertThat(channel.getProfile().getImage().getId()).isEqualTo(command.imageId());
        }
    }
}
