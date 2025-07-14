package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.post.dto.PostResponse;

public record JoinedInfoChannelResponse(
    Long channelId,
    String title,
    String profile,
    String description,
    ChannelType type,
    ChannelSettingResponse settings,
    Long memberCount,
    PostResponse latestPost
) {
    public static JoinedInfoChannelResponse from(InfoChannelOverview infoChannelOverview) {
        Channel channel = infoChannelOverview.getInfoChannel();
        return new JoinedInfoChannelResponse(
                channel.getId(),
                channel.getTitle(),
                channel.getProfile(),
                channel.getDescription(),
                channel.getChannelType(),
                ChannelSettingResponse.from(channel.getSetting()),
                infoChannelOverview.getMemberCount(),
                PostResponse.from(infoChannelOverview.getLatestPost())
        );
    }
}
