package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelUpdateRequest;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ChannelUpdater {
    private final ChannelRepository channelRepository;
    private final ChannelFinder channelFinder;
    private final ChannelProfileService channelProfileService;

    public void updateChannel(
            Long channelId,
            ChannelUpdateRequest request
    ) {
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        if(!channel.getTitle().equals(request.title())) {
            if(isDuplicateTitle(request.title()))
                throw new ConflictException(ErrorCode.DUPLICATE_CHANNEL_TITLE);
            channel.updateTitle(request.title());
        }

        if(!channel.getDescription().equals(request.description())) {
            channel.updateDescription(request.description());
        }

        channelProfileService.updateImage(channel.getId(), request.imageId());

        ChannelSetting setting = channel.getSetting();
        boolean isSettingChanged =
                setting.getContentVisibility() != request.settings().contentVisibility()
                || setting.getJoinPolicy() != request.settings().joinPolicy();
        if(isSettingChanged) {
            setting.updateSettings(request.settings().contentVisibility(), request.settings().joinPolicy());
        }
    }

    private boolean isDuplicateTitle(String title) {
        return channelRepository.existsByTitle(title);
    }
}
