package com.zonbeozon.channel.service;

import com.zonbeozon.channel.controller.ChannelUpdateRequest;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelUpdateException;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class ChannelUpdateTest extends ChannelServiceTest {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelMemberService channelMemberService;
    @Autowired
    private ChannelRepository channelRepository;

    private Long createdChannelId;
    private Member owner;
    private Member notOwner;

    private ChannelUpdateRequest validUpdateRequest = new ChannelUpdateRequest(
            "otherName",
            "otherDescription",
            "otherEmtpyProfile",
            ChannelContentOpenLevel.PRIVATE,
            ChannelJoinLevel.DENY,
            ChannelSearchLevel.PRIVATE
    );

    @Autowired
    public ChannelUpdateTest(MemberService memberService) {
        super(memberService);
    }

    @BeforeEach
    void setup() {
        owner = serverUser_1;
        notOwner = serverUser_2;
        createdChannelId = channelService.addChannel(validChannelCreateCommand_1, owner);
    }

    @Test
    @DisplayName("채널 Owner가 아니라면 채널 업데이트를 호출시 예외가 발생한다")
    void throwExceptionWhenNonOwnerTriesToUpdateChannel() {
        channelMemberService.joinAsMember(notOwner, createdChannelId);
        assertThatThrownBy(() -> channelService.updateChannel(notOwner, createdChannelId, validUpdateRequest))
                .isInstanceOf(ChannelUpdateException.class)
                .satisfies((e) -> {
                    ChannelUpdateException exception = (ChannelUpdateException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelUpdateException.ErrorCode.ACCESS_DENIED);
                });
    }

    @Test
    @DisplayName("중복 채널 명이 있다면 예외가 발생한다")
    void throwExceptionWhenDuplicateChannelTitleProvided() {
        ChannelCreateCommand validChannelCreateCommand = new ChannelCreateCommand(
                validUpdateRequest.title(),
                "description",
                "emtpyProfile",
                ChannelContentOpenLevel.PUBLIC,
                ChannelType.COMMUNITY_INFO,
                ChannelJoinLevel.OPEN,
                ChannelSearchLevel.PUBLIC
        );
        channelService.addChannel(validChannelCreateCommand, owner);

        assertThatThrownBy(() -> channelService.updateChannel(owner, createdChannelId, validUpdateRequest))
                .isInstanceOf(ChannelUpdateException.class)
                .satisfies((e) -> {
                    ChannelUpdateException exception = (ChannelUpdateException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelUpdateException.ErrorCode.DUPLICATE_CHANNEL_TITLE);
                });
    }

    @Test
    @DisplayName("채널 Setting조합이 잘못된 조합(SearchLevel이 Private, ContentOpenLevel이 Public)이라면 예외가 발생한다.")
    void throwExceptionWhenInvalidSettingCombinationProvided() {
        ChannelUpdateRequest invalidCombinationUpdateRequest = new ChannelUpdateRequest(
                validChannelCreateCommand_1.title(),
                "description",
                "emtpyProfile",
                ChannelContentOpenLevel.PUBLIC,
                ChannelJoinLevel.DENY,
                ChannelSearchLevel.PRIVATE
        );

        assertThatThrownBy(() -> channelService.updateChannel(owner, createdChannelId, invalidCombinationUpdateRequest))
                .isInstanceOf(ChannelUpdateException.class)
                .satisfies((e) -> {
                    ChannelUpdateException exception = (ChannelUpdateException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelUpdateException.ErrorCode.INVALID_CHANNEL_SETTING_COMBINATION);
                });
    }

    @Test
    @DisplayName("업데이트가 정상적으로 수행되어야 한다.")
    void updateChannelSuccessfullyWhenValidRequestAndOwner() {
        channelService.updateChannel(owner, createdChannelId, validUpdateRequest);
        Channel channel = channelRepository.findById(createdChannelId)
                .orElseThrow(() -> new RuntimeException("채널 Id에 맞는 채널이 존재하지 않습니다."));

        assertThat(channel.getTitle()).isEqualTo(validUpdateRequest.title());
        assertThat(channel.getDescription()).isEqualTo(validUpdateRequest.description());
        assertThat(channel.getProfile()).isEqualTo(validUpdateRequest.profile());
        assertThat(channel.getContentOpenLevel()).isEqualTo(validUpdateRequest.contentOpenLevel());
        assertThat(channel.getJoinLevel()).isEqualTo(validUpdateRequest.joinLevel());
        assertThat(channel.getSearchLevel()).isEqualTo(validUpdateRequest.searchLevel());
    }
}
