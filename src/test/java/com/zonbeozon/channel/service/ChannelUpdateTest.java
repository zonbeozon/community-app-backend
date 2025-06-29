package com.zonbeozon.channel.service;

import com.zonbeozon.channel.controller.ChannelUpdateRequest;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAccessDeniedException;
import com.zonbeozon.channel.exception.ChannelBadRequestException;
import com.zonbeozon.channel.repository.ChannelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class ChannelUpdateTest extends BaseChannelTest {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelMemberService channelMemberService;
    @Autowired
    private ChannelRepository channelRepository;

    private ChannelUpdateRequest validUpdateRequest = new ChannelUpdateRequest(
            "otherName",
            "otherDescription",
            "otherEmtpyProfile",
            ChannelContentOpenLevel.PRIVATE,
            ChannelJoinLevel.DENY,
            ChannelSearchLevel.PRIVATE
    );

    @Test
    @DisplayName("채널 Owner가 아니라면 채널 업데이트를 호출시 예외가 발생한다")
    void throwExceptionWhenNonOwnerTriesToUpdateChannel() {
        channelMemberService.joinAsMember(member_2, channel_1_id);
        assertThatThrownBy(() -> channelService.updateChannel(member_2, channel_1_id, validUpdateRequest))
                .isInstanceOf(ChannelAccessDeniedException.class)
                .satisfies((e) -> {
                    ChannelAccessDeniedException exception = (ChannelAccessDeniedException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAccessDeniedException.ErrorCode.MODIFY_CHANNEL_METADATA_FORBIDDEN);
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
        channelService.addChannel(validChannelCreateCommand, channel_1_owner);

        assertThatThrownBy(() -> channelService.updateChannel(channel_1_owner, channel_1_id, validUpdateRequest))
                .isInstanceOf(ChannelBadRequestException.class)
                .satisfies((e) -> {
                    ChannelBadRequestException exception = (ChannelBadRequestException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelBadRequestException.ErrorCode.DUPLICATE_CHANNEL_TITLE);
                });
    }

    @Test
    @DisplayName("채널 Setting조합이 잘못된 조합(SearchLevel이 Private, ContentOpenLevel이 Public)이라면 예외가 발생한다.")
    void throwExceptionWhenInvalidSettingCombinationProvided() {
        ChannelUpdateRequest invalidCombinationUpdateRequest = new ChannelUpdateRequest(
                ChannelFixture.channelCreateCommand_1.title(),
                "description",
                "emtpyProfile",
                ChannelContentOpenLevel.PUBLIC,
                ChannelJoinLevel.DENY,
                ChannelSearchLevel.PRIVATE
        );

        assertThatThrownBy(() -> channelService.updateChannel(channel_1_owner, channel_1_id, invalidCombinationUpdateRequest))
                .isInstanceOf(ChannelBadRequestException.class)
                .satisfies((e) -> {
                    ChannelBadRequestException exception = (ChannelBadRequestException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelBadRequestException.ErrorCode.INVALID_CHANNEL_SETTING_COMBINATION);
                });
    }

    @Test
    @DisplayName("업데이트가 정상적으로 수행되어야 한다.")
    void updateChannelSuccessfullyWhenValidRequestAndOwner() {
        channelService.updateChannel(channel_1_owner, channel_1_id, validUpdateRequest);
        Channel channel = channelRepository.findById(channel_1_id)
                .orElseThrow(() -> new RuntimeException("채널 Id에 맞는 채널이 존재하지 않습니다."));

        assertThat(channel.getTitle()).isEqualTo(validUpdateRequest.title());
        assertThat(channel.getDescription()).isEqualTo(validUpdateRequest.description());
        assertThat(channel.getProfile()).isEqualTo(validUpdateRequest.profile());
        assertThat(channel.getContentOpenLevel()).isEqualTo(validUpdateRequest.contentOpenLevel());
        assertThat(channel.getJoinLevel()).isEqualTo(validUpdateRequest.joinLevel());
        assertThat(channel.getSearchLevel()).isEqualTo(validUpdateRequest.searchLevel());
    }
}
