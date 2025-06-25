package com.zonbeozon.channel.service.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.channel.repository.JoinedChannelDto;

import java.time.LocalDateTime;

public record JoinedChannelResponse(
        Long channelId,
        String title,
        String profile,
        String description,
        ChannelType channelType,
        ChannelJoinLevel channelJoinLevel,
        ChannelContentOpenLevel contentOpenLevel,
        Long memberCount,
        String latestPostContent,
        LocalDateTime latestPostCreatedAt
) {

    public static JoinedChannelResponse from(
            JoinedChannelDto joinedChannelDto
    ) {
        Channel channel = joinedChannelDto.getChannel();
        return new JoinedChannelResponse(
                channel.getId(),
                channel.getTitle(),
                channel.getProfile(),
                channel.getDescription(),
                channel.getType(),
                channel.getJoinLevel(),
                channel.getContentOpenLevel(),
                joinedChannelDto.getMemberCount(),
                joinedChannelDto.getLatestPostContent(),
                joinedChannelDto.getLatestPostCreatedAt()
        );
    }
}
