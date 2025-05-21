package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.ChannelAction;
import com.zonbeozon.channel.entity.ChannelMember;
import org.springframework.lang.Nullable;

public interface ChannelPermissionValidator {
    void validate(ChannelAction action, ChannelContext context, @Nullable ChannelMember target);
    default void validate(ChannelAction action, ChannelContext context) {
        validate(action, context, null);
    }
}
