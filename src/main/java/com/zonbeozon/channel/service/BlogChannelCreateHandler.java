package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelCreateCommand;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class BlogChannelCreateHandler implements ChannelCreateStrategy {
    @Override
    public Channel createChannel(ChannelCreateCommand command, Member creator) {
        return new BlogChannel(
                command.title(),
                command.description(),
                new ChannelSetting(command.visibility(), command.joinPolicy())
        );
    }

    @Override
    public boolean isSupport(ChannelCreateCommand command) {
        return command.type() == ChannelType.BLOG;
    }
}
