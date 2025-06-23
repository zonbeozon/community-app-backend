package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelSearchLevel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.channel.exception.ChannelDeleteException;
import com.zonbeozon.channel.exception.ChannelMemberNotFoundException;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.member.service.MemberService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

public class ChannelDeleteTest extends ChannelServiceTest {
    @Autowired
    public ChannelDeleteTest(MemberService memberService) {
        super(memberService);
    }

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Autowired
            private EntityManager em;

    ChannelCreateCommand command = new ChannelCreateCommand(
            "title",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelMemberService channelMemberService;

    @Test
    @DisplayName("채널 Owner라면 채널삭제를 수행할 수 있다.")
    void ownerCanDeleteChannelSuccessfully() {
        Long id = channelService.addChannel(command, serverUser);
        channelRepository.findById(id);
        channelService.deleteChannel(serverUser, id);
    }

    @Test
    @DisplayName("채널 Owner가 아니라면 예외를 발생시킨다.")
    void deleteByNonOwnerThrowsAccessDenied() {
        Long id = channelService.addChannel(command, serverUser);
        channelMemberService.joinAsMember(serverAdmin, id);

        assertThatThrownBy(() -> channelService.deleteChannel(serverAdmin, id))
                .isInstanceOf(ChannelDeleteException.class)
                .satisfies(e -> {
                    ChannelDeleteException exception = (ChannelDeleteException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelDeleteException.ErrorCode.ACCESS_DENIED);
                });
    }

    @Test
    @DisplayName("채널에 가입한 유저가 아니라면 예외를 발생시킨다.")
    void deleteByNonMemberThrowsMemberNotFound() {
        Long id = channelService.addChannel(command, serverUser);

        assertThatThrownBy(() -> channelService.deleteChannel(serverAdmin, id))
                .isInstanceOf(ChannelMemberNotFoundException.class);
    }
}
