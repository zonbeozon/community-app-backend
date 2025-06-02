package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ChannelMemberResolver {
    private final ChannelServiceImpl channelServiceImpl;
    private final ChannelMemberServiceImpl channelMemberServiceImpl;

    public void findChannelMemberThenConsume(Member member, Long channelId, Consumer<ChannelMember> consumer) {
        findChannelMemberThenConsume(member, channelId, consumer, false);
    }

    public void findChannelMemberThenConsume(Member member, Long channelId, Consumer<ChannelMember> consumer, boolean nullable) {
        Channel channel = channelServiceImpl.getChannelByIdOrThrow(channelId);

        ChannelMember channelMember;
        if(nullable) {
            channelMember = channelMemberServiceImpl.getByMemberAndChannel(member, channel).orElse(null);
        } else {
            channelMember = channelMemberServiceImpl.getByMemberAndChannelOrThrow(member, channel);
        }
        consumer.accept(channelMember);
    }

    public  <R> R findChannelMemberThenApply(Member member, Long channelId,  Function<ChannelMember, R> function) {
        return findChannelMemberThenApply(member, channelId, function, false);
    }

    public <R> R findChannelMemberThenApply(Member member, Long channelId, Function<ChannelMember, R> function, boolean nullable) {
        Channel channel = channelServiceImpl.getChannelByIdOrThrow(channelId);

        ChannelMember channelMember;
        if(nullable) {
            channelMember = channelMemberServiceImpl.getByMemberAndChannel(member, channel).orElse(null);
        } else {
            channelMember = channelMemberServiceImpl.getByMemberAndChannelOrThrow(member, channel);
        }
        return function.apply(channelMember);
    }
}
