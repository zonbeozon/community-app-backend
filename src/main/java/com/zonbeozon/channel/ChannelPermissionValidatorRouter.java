package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelAction;
import com.zonbeozon.channel.entity.ChannelMember;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChannelPermissionValidatorRouter implements ChannelPermissionValidator {
    private final List<ChannelPermissionValidatorImpl> permissionValidators;

    @Override
    public void validate(ChannelAction action, ChannelContext context, ChannelMember target) {
        getPermissionValidator(context.getChannel().getChannelType()).validate(action, context, target);
    }

    private ChannelPermissionValidatorImpl getPermissionValidator(Channel.Type channelType) {
        return permissionValidators.stream()
                .filter(validator -> validator.isSupported(channelType))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No permission validator found for channel type " + channelType));
    }
}
