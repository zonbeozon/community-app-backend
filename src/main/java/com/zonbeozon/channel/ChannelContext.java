package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelContext {
    private Long channelId;
    private Member member;
    private ChannelMember channelMember;
    private Channel channel;

    public static ChannelContext with(Long channelId, Member member) {
        ChannelContext context = new ChannelContext();
        context.channelId = channelId;
        context.member = member;
        return context;
    }

    public static ChannelContext with(Long channelId) {
        ChannelContext context = new ChannelContext();
        context.channelId = channelId;
        context.member = null;
        return context;
    }
}
