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

    public Channel findById(Long id) {
        return channelRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }
}
