package com.zonbeozon.channel.service;

import com.zonbeozon.channel.ChannelContext;
import com.zonbeozon.channel.ChannelPermissionValidator;
import com.zonbeozon.channel.UseChannelContext;
import com.zonbeozon.channel.controller.ChannelCreateRequest;
import com.zonbeozon.channel.controller.ChannelInfoUpdateRequest;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelNotFoundException;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.channel.service.dto.*;
import com.zonbeozon.common.EntityValidator;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@UseChannelContext
public class ChannelService {
    private final ChannelMemberService channelMemberService;
    private final ChannelRepository channelRepository;
    private final ChannelPermissionValidator permissionValidator;
    private final EntityValidator entityValidator;

    public Long addChannel(ChannelCreateRequest request, Member requester) {
        Channel channel = Channel.create(request);
        entityValidator.validate(channel);
        permissionValidator.validate(ChannelAction.CREATION, ChannelContext.with(null, requester));
        channelRepository.save(channel);
        channelMemberService.subscribeChannel(requester, channel, ChannelRole.CHANNEL_OWNER);
        return channel.getId();
    }

    public void updateChannelInfo(ChannelContext channelContext, ChannelInfoUpdateRequest channelInfoUpdateRequest) {
        permissionValidator.validate(ChannelAction.INFO_MODIFICATION, channelContext);
        channelContext.getChannel().updateInfo(channelInfoUpdateRequest.title(), channelInfoUpdateRequest.description());
        entityValidator.validate(channelContext.getChannel());
    }

    public void delete(ChannelContext channelContext) {
        permissionValidator.validate(ChannelAction.DELETION, channelContext);
        channelRepository.delete(channelContext.getChannel());
    }

    public void changeOpenLevel(ChannelContext channelContext, Channel.OpenLevel openLevel) {
        permissionValidator.validate(ChannelAction.INFO_MODIFICATION, channelContext);
        channelContext.getChannel().changeOpenLevel(openLevel);
    }

    public ChannelResponseWrapper createMemberJoinedChannelResponse(Member member) {
        List<ChannelResponse> channels = channelMemberService.getByMember(member).stream()
                .map(ChannelMember::getChannel)
                .map(channel -> ChannelResponse.from(channel, channelMemberService.getMemberCountByChannel(channel)))
                .toList();
        return new ChannelResponseWrapper(channels, channels.size());
    }

    public ChannelResponseWrapper createChannelSearchResponse(
            String searchParam,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            Channel.Type type
    ) {
        List<ChannelResponse> channels = channelRepository.searchByKeyword(searchParam, page, size, sort, direction, type, Channel.OpenLevel.PUBLIC).stream()
                .map(channelWithMemberCount -> ChannelResponse.from(channelWithMemberCount.getChannel(), channelWithMemberCount.getMemberCount()))
                .toList();

        return new ChannelResponseWrapper(channels, channels.size());
    }

    public ChannelResponse getChannelResponse(Channel channel) {
        int memberCount = channelMemberService.getMemberCountByChannel(channel);
        return ChannelResponse.from(channel, memberCount);
    }

    public Channel getChannelByIdOrThrow(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId + "은 존재하지 않는 channelId입니다."));
    }
}
