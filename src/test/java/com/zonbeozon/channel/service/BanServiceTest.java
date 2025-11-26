package com.zonbeozon.channel.service;

import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.dto.BannedChannelMemberDto;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class BanServiceTest extends AbstractChannelIntegrationTest {
    @Autowired
    private ChannelMemberBanService channelMemberBanService;
    @Autowired
    private ChannelMemberAssembler channelMemberAssembler;
    @Autowired
    private ChannelMemberJoiner channelMemberJoiner;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    private Member member;
    private Channel channel;
    private ChannelMember channelMember;

    @BeforeEach
    void setup() {
        channel = testChannelService.createAndSave();
        member = testMemberService.createAndSave();
        channelMember = testChannelService.joinAsMember(channel, member);
    }

    @Test
    @DisplayName("벤 된 맴버는 채널에서 탈퇴된다")
    void banShouldRemoveMemberFromChannel() {
        channelMemberBanService.ban(channel.getId(), member.getId(), null);
        Assertions.assertThat(channelMemberRepository.findById(channelMember.getId())).isEmpty();
    }

    @Test
    @DisplayName("벤 된 맴버는 재입장이 불가능하다")
    void bannedMemberCannotRejoinChannel() {
        channelMemberBanService.ban(channel.getId(), member.getId(), null);
        Assertions.assertThatThrownBy(() -> channelMemberJoiner.joinAsMember(channel.getId(), member.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("벤 된 맴버는 조회가능하다.")
    void bannedMemberShouldAppearInBannedList() {
        channelMemberBanService.ban(channel.getId(), member.getId(), null);
        Page<BannedChannelMemberDto> bannedChannelMembers = channelMemberAssembler.getPagedBannedChannelMember(channel.getId(), PageRequest.of(0, 10));
        Assertions.assertThat(bannedChannelMembers.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(bannedChannelMembers.getContent().getFirst().memberId()).isEqualTo(member.getId());
    }
}
