package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.ChannelInfoDto;
import com.zonbeozon.channel.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelRepositoryCustom {
    Optional<Channel> findByIdWithProfile(Long channelId);
    Optional<ChannelInfoDto> findByIdWithProfileAndChannelMemberCount(Long channelId);
    List<ChannelInfoDto> findByIdInWithProfileAndChannelMemberCount(List<Long> channelIds);
    void updateMemberCount(Long channelId, int delta);
}
