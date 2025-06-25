package com.zonbeozon.channel.service;

import com.zonbeozon.channel.exception.ChannelDeleteException;
import com.zonbeozon.channel.exception.ChannelMemberNotFoundException;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class ChannelDeleteTest extends ChannelServiceTest {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelMemberService channelMemberService;

    private Long createdChannelId;
    private Member owner;
    private Member notOwner;

    @Autowired
    public ChannelDeleteTest(MemberService memberService) {
        super(memberService);
    }

    @BeforeEach
    void setup() {
        owner = serverUser_1;
        notOwner = serverUser_2;
        createdChannelId = channelService.addChannel(validChannelCreateCommand_1, serverUser_1);
    }

    @Test
    @DisplayName("채널 Owner라면 채널삭제를 수행할 수 있다.")
    void ownerCanDeleteChannelSuccessfully() {
        channelRepository.findById(createdChannelId);
        channelService.deleteChannel(owner, createdChannelId);
    }

    @Test
    @DisplayName("채널 Owner가 아니라면 예외를 발생시킨다.")
    void deleteByNonOwnerThrowsAccessDenied() {
        channelMemberService.joinAsMember(serverUser_2, createdChannelId);

        assertThatThrownBy(() -> channelService.deleteChannel(notOwner, createdChannelId))
                .isInstanceOf(ChannelDeleteException.class)
                .satisfies(e -> {
                    ChannelDeleteException exception = (ChannelDeleteException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelDeleteException.ErrorCode.ACCESS_DENIED);
                });
    }

    @Test
    @DisplayName("채널에 가입한 유저가 아니라면 예외를 발생시킨다.")
    void deleteByNonMemberThrowsMemberNotFound() {
        assertThatThrownBy(() -> channelService.deleteChannel(notOwner, createdChannelId))
                .isInstanceOf(ChannelMemberNotFoundException.class);
    }
}
