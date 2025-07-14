package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;

public interface ModifyChannelRoleHandler {
    void handle(ChannelMember requester, ChannelMember targetChannelMember, ChannelRole newRole);
    boolean isSupport(ChannelRole newRole);
}
