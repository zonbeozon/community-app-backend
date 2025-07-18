package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;

public class TestChannelMemberBuilder {
    private Member member;
    private Channel channel;
    private ChannelRole channelRole = ChannelRole.CHANNEL_MEMBER;
    private ChannelMemberStatus channelMemberStatus = ChannelMemberStatus.ACTIVE;

    public TestChannelMemberBuilder(Member member, Channel channel) {
        this.member = member;
        this.channel = channel;
    }

    public TestChannelMemberBuilder withRole(ChannelRole channelRole) {
        this.channelRole = channelRole;
        return this;
    }

    public TestChannelMemberBuilder withStatus(ChannelMemberStatus channelMemberStatus) {
        this.channelMemberStatus = channelMemberStatus;
        return this;
    }

    public ChannelMember build() {
        return ChannelMember.create(member, channel, channelRole, channelMemberStatus);
    }

    public ChannelMember persist(EntityManager entityManager) {
        ChannelMember channelMember = build();
        entityManager.persist(channelMember);
        return channelMember;
    }
}
