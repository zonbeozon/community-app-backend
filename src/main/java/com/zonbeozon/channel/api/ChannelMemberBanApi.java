package com.zonbeozon.channel.api;

import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.channel.service.ChannelMemberBanService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@RequiredArgsConstructor
@Transactional
public class ChannelMemberBanApi {
    private final ChannelMemberBanService channelMemberBanService;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;

    public void ban(Long channelId, Long memberId, @Nullable String reason) {
        if(!channelAuthorizationCheckService.hasHigherRoleThanTargetMember(channelId, memberId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberBanService.ban(channelId, memberId, reason);
    }

    public void unban(Long channelId, Long memberId) {
        if(!channelAuthorizationCheckService.isOwner(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberBanService.unban(channelId, memberId);
    }
}
