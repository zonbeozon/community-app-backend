package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.ChannelWithMemberCount;
import com.zonbeozon.channel.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepositoryCustom {
    Optional<Channel> findByIdWithProfile(Long channelId);
    Optional<ChannelWithMemberCount> findByIdWithProfileAndMemberCount(Long channelId);
    List<ChannelWithMemberCount> findByIdInWithProfileAndMemberCount(List<Long> channelIds);
}
