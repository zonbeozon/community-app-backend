package com.zonbeozon.channel.api;

import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.channel.service.ChannelRemover;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@ApiComponent
public class ChannelRemoveApi {
    private final ChannelRemover channelRemover;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;

    public void removeChannel(Long channelId) {
        if(!channelAuthorizationCheckService.isOwner(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelRemover.removeChannel(channelId);
    }
}
