package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelAddCommand;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.entity.InfoChannel;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class CommunityInfoChannelCreateHandler implements ChannelCreateStrategy {
    @Override
    public Channel createChannel(ChannelAddCommand command, Member creator) {
        return new InfoChannel(
                command.title(),
                command.description(),
                command.profile(),
                new ChannelSetting(command.contentVisibility(), command.joinPolicy(), command.searchScope()),
                ChannelCreatorType.COMMUNITY
        );
    }

    @Override
    public boolean isSupport(ChannelAddCommand command) {
        return command.type() == ChannelType.INFO && command.creatorType() == ChannelCreatorType.COMMUNITY;
    }
}
