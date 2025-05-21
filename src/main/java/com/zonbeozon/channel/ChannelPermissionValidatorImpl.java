package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelAction;
import com.zonbeozon.channel.entity.ChannelMember;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
class ChannelPermissionValidatorImpl implements ChannelPermissionValidator {
    private final Map<ChannelAction, ChannelPermissionValidateHandler> handlers;
    private final Channel.Type channelType;

    @Override
    public void validate(ChannelAction action, ChannelContext context, ChannelMember target) {
        ChannelPermissionValidateHandler matchedHandler = handlers.get(action);
        if(matchedHandler == null) {
            throw new IllegalStateException(action + "을 처리할 수 있는 헨들러가 등록되지 않았습니다.");
        }
        matchedHandler.handle(action, context, target);
    }

    public boolean isSupported(Channel.Type channelType) {
        return this.channelType == channelType;
    }
}
