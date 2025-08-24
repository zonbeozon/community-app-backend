package com.zonbeozon.channel.service.cache;

public interface ChannelCacheInvalidator {
    void invalidate(Long channelId);
}
