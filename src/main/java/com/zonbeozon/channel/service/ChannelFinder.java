package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelFinder {
    private final ChannelRepository channelRepository;

    public Channel findByIdElseThrow(Long channelId) {
        return channelRepository.findById(channelId).orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    public Channel findChannelByIdWithChannelProfileElseThrow(Long channelId) {
        return channelRepository.findByIdWithProfile(channelId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }
}
