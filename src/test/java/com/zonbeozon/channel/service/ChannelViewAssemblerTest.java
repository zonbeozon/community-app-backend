package com.zonbeozon.channel.service;

import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.dto.ChannelViewDto;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.service.assembler.ChannelViewAssembler;
import com.zonbeozon.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ChannelViewAssemblerTest extends AbstractChannelIntegrationTest {
    @Autowired
    private ChannelViewAssembler channelViewAssembler;

    private Channel channel;
    private Member member;

    @BeforeEach
    void setup() {
        channel = testBlogChannelService.createAndSave();
        member = testMemberService.createAndSave();
    }

    @Test
    @DisplayName("참가한 채널이라면 채널 맴버쉽 정보가 추가된다.")
    void shouldIncludeMembershipWhenMemberJoinedChannel() {
        testBlogChannelService.joinAsMember(channel, member);
        ChannelViewDto channelView = channelViewAssembler.getChannelView(channel.getId(), member.getId());
        Assertions.assertThat(channelView.isJoined()).isTrue();
        Assertions.assertThat(channelView.membership().memberId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("참가하지 않은 채널이라면 채널 정보만 리턴한다.")
    void shouldNotIncludeMembershipWhenMemberNotJoined() {
        ChannelViewDto channelView = channelViewAssembler.getChannelView(channel.getId(), member.getId());
        Assertions.assertThat(channelView.isJoined()).isFalse();
        Assertions.assertThat(channelView.membership()).isNull();
    }
}
