package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelRemover {
    private final ChannelFinder channelFinder;
    private final ChannelRepository channelRepository;

    public void removeChannel(Long channelId) {
        Channel channel = channelFinder.findById(channelId);
        channelRepository.delete(channel);
    }
}
