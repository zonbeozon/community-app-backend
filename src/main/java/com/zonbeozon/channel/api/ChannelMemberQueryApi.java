package com.zonbeozon.channel.api;

import com.zonbeozon.channel.dto.BannedChannelMemberDto;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.dto.PendingChannelMemberDto;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.channel.service.assembler.ChannelMemberQueryService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelMemberQueryApi {
    private final ChannelMemberQueryService channelMemberQueryService;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;

    public Page<ChannelMemberDto> getActiveChannelMembers(Long channelId, Pageable pageable) {
        if(!channelAuthorizationCheckService.canAccessChannelContent(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);

        return channelMemberQueryService.getPagedActiveChannelMember(channelId, pageable);
    }

    public Page<BannedChannelMemberDto> getBannedChannelMembers(Long channelId, Pageable pageable) {
        if(!channelAuthorizationCheckService.isOwner(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);

        return channelMemberQueryService.getPagedBannedChannelMember(channelId, pageable);
    }

    public Page<PendingChannelMemberDto> getPendingChannelMembers(Long channelId, Pageable pageable) {
        if(!channelAuthorizationCheckService.isOwner(channelId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);

        return channelMemberQueryService.getPagedPendingChannelMember(channelId, pageable);
    }
}
