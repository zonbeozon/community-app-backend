package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.test.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.dto.ChannelInfosWithMembershipDto;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.service.assembler.JoinedChannelAssembler;
import com.zonbeozon.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class JoinedChannelAssemblerTest extends AbstractChannelIntegrationTest {

    @Autowired
    private JoinedChannelAssembler joinedChannelAssembler;

    private Channel channel_1;
    private Channel channel_2;

    private Member requester;

    @BeforeEach
    void setup() {
        requester = testMemberService.createAndSave();
        channel_1 = testChannelService.createAndSave("channel_1");
        channel_2 = testChannelService.createAndSave("channel_2");

        testChannelService.joinAsMember(channel_1, requester);
    }

    @Test
    @DisplayName("주어진 맴버가 포함된 채널만 가져와야 한다.")
    void returnsOnlyChannelsJoinedByMember() {
        ChannelInfosWithMembershipDto response = joinedChannelAssembler.getJoinedChannels(requester.getId());
        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).channelInfo().channelId()).isEqualTo(channel_1.getId());
    }

    @Test
    @DisplayName("요청자의 채널에서의 정보가 포함되어야 한다.")
    void includeRequesterMetadataInJoinedChannelResponse() {
        ChannelInfosWithMembershipDto response = joinedChannelAssembler.getJoinedChannels(requester.getId());
        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).membership().memberId()).isEqualTo(requester.getId());
        assertThat(response.channels().get(0).membership().channelRole()).isEqualTo(ChannelRole.CHANNEL_MEMBER);
    }
}
