package com.zonbeozon.channel.service;

import com.zonbeozon.channel.enums.ChannelRole;
import org.springframework.stereotype.Component;

@Component
public class SimpleModifyChannelRoleHandler extends AbstractModifyChannelRoleHandler implements ModifyChannelRoleHandler {
    @Override
    public boolean isSupport(ChannelRole newRole) {
        return newRole != ChannelRole.CHANNEL_OWNER;
    }
}
