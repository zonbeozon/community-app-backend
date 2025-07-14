package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;

public abstract class AbstractModifyChannelRoleHandler implements ModifyChannelRoleHandler {
    @Override
    public void handle(ChannelMember requester, ChannelMember targetChannelMember, ChannelRole newRole) {
        if(requester.equals(targetChannelMember)) {
            throw new BadRequestException(ErrorCode.CANNOT_TARGET_SELF);
        }
        if(targetChannelMember.getRole() == newRole){
            throw new ConflictException(ErrorCode.SAME_STATE_CANNOT_BE_UPDATED);
        }
        targetChannelMember.updateRole(newRole);
    }
}
