package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;

public interface ChannelEntityQueryService {
    Channel getChannelByIdOrThrow(Long channelId);
}
