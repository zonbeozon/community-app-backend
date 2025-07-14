package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import org.springframework.stereotype.Component;

@Component
public class OwnerModifyChannelRoleHandler extends AbstractModifyChannelRoleHandler implements ModifyChannelRoleHandler {
    @Override
    public void handle(ChannelMember requester, ChannelMember targetChannelMember, ChannelRole newRole) {
        super.handle(requester, targetChannelMember, newRole);
        //기존 Owner를 admin으로 강등
        requester.updateRole(ChannelRole.CHANNEL_ADMIN);
    }
    @Override
    public boolean isSupport(ChannelRole newRole) {
        return newRole == ChannelRole.CHANNEL_OWNER;
    }
}
