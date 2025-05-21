package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.ChannelAction;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.exception.ChannelAccessDeniedException;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

interface ChannelPermissionValidateHandler {
    void handle(ChannelAction action, ChannelContext context, ChannelMember target);

    static ChannelPermissionValidateHandler of(Predicate<ChannelContext> predicate) {
        return (action, context, target) -> {
            if (!predicate.test(context)) {
                throw new ChannelAccessDeniedException(action + "을 수행할 권한이 없습니다.");
            }
        };
    }

    static ChannelPermissionValidateHandler of(BiPredicate<ChannelContext, ChannelMember> predicate) {
        return (action, context, target) -> {
            if (!predicate.test(context, target)) {
                throw new ChannelAccessDeniedException(action + "을 수행할 권한이 없습니다.");
            }
        };
    }
}
