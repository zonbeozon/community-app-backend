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
    private final ChannelEntityQueryService channelEntityQueryService;
    private final ChannelMemberEntityQueryService channelMemberEntityQueryService;

    public void findChannelMemberThenConsume(Member member, Long channelId, Consumer<ChannelMember> consumer) {
        findChannelMemberThenConsume(member, channelId, consumer, false);
    }

    public void findChannelMemberThenConsume(Member member, Long channelId, Consumer<ChannelMember> consumer, boolean nullable) {
        consumer.accept(findChannelMember(member, channelId, nullable));
    }

    public  <R> R findChannelMemberThenApply(Member member, Long channelId,  Function<ChannelMember, R> function) {
        return findChannelMemberThenApply(member, channelId, function, false);
    }

    public <R> R findChannelMemberThenApply(Member member, Long channelId, Function<ChannelMember, R> function, boolean nullable) {
        return function.apply(findChannelMember(member, channelId, nullable));
    }

    private ChannelMember findChannelMember(Member member, Long channelId, boolean nullable) {
        Channel channel = channelEntityQueryService.getChannelByIdOrThrow(channelId);
        if(nullable) {
            return channelMemberEntityQueryService.getChannelMember(member, channel).orElse(null);
        }
        return channelMemberEntityQueryService.getChannelMemberOrThrow(member, channel);
    }
}
