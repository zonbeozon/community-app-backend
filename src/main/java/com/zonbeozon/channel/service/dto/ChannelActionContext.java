package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.ChannelRole;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChannelActionContext {
    private final Channel channel;
    private final ChannelMember requestChannelMember;
    private final Member requestMember;
    private final ChannelMember targetChannelMember;

    public static ChannelActionContext of(ChannelMember requestChannelMember, ChannelMember targetChannelMember) {
        return new ChannelActionContext(
                requestChannelMember.getChannel(),
                requestChannelMember,
                requestChannelMember.getMember(),
                targetChannelMember
        );
    }

    public static ChannelActionContext of(ChannelMember requestChannelMember) {
        return new ChannelActionContext(
                requestChannelMember.getChannel(),
                requestChannelMember,
                requestChannelMember.getMember(),
                null);
    }

    public static ChannelActionContext of(Channel channel, Member requestMember) {
        return new ChannelActionContext(
                channel,
                null,
                requestMember,
                null
        );
    }

    public Channel getChannel() {
        return channel;
    }

    public ChannelMember getRequestChannelMember() {
        Assert.notNull(targetChannelMember, "외부로 부터 받은 ChannelActionContext 내부의 requestChannelMember 필드가 null 입니다.");
        return requestChannelMember;
    }

    public Member getRequestMember() {
        return requestMember;
    }

    public ChannelMember getTargetChannelMember() {
        Assert.notNull(targetChannelMember, "외부로 부터 받은 ChannelActionContext 내부의 targetChannelMember 필드가 null 입니다.");
        return targetChannelMember;
    }

    public static boolean allowAll(ChannelActionContext context) {
        return true;
    }

    public static boolean denyAll(ChannelActionContext context) {
        return false;
    }

    public static boolean isChannelOwner(ChannelActionContext context) {
        return context.getRequestChannelMember().getRole() == ChannelRole.CHANNEL_OWNER;
    }

    public static boolean isChannelAdmin(ChannelActionContext context) {
        return context.getRequestChannelMember().getRole() == ChannelRole.CHANNEL_ADMIN;
    }

    public static boolean isServerAdmin(ChannelActionContext context) {
        return context.getRequestMember().getRole() == ServerRole.ADMIN;
    }

    public static boolean isHigherLevel(ChannelActionContext context) {
        return context.getRequestChannelMember().getRole().isHigherThan(context.getTargetChannelMember().getRole());
    }
}
