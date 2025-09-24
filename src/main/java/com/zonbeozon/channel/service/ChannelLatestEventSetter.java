package com.zonbeozon.channel.service;

import com.zonbeozon.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class ChannelLatestEventSetter {
    private final ChannelRepository channelRepository;

    public void update(Long channelId, LocalDateTime eventTime) {
        channelRepository.updateLatestEventTime(channelId, eventTime);
    }

    public void updateAsNow(Long channelId) {
        update(channelId, LocalDateTime.now());
    }

    public void updateAsNull(Long channelId) {
        update(channelId, null);
    }
}
