package com.zonbeozon.channel.service;

import com.zonbeozon.channel.controller.ChannelInfoUpdateRequest;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAddBadRequestException;
import com.zonbeozon.channel.exception.ChannelBadRequestException;
import com.zonbeozon.channel.exception.ChannelNotFoundException;
import com.zonbeozon.channel.exception.ErrorCode;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.channel.repository.ChannelWithMemberCount;
import com.zonbeozon.channel.service.dto.*;
import com.zonbeozon.common.EntityValidator;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
class ChannelServiceImpl implements ChannelEntityQueryService {
    private final ChannelMemberServiceImpl channelMemberServiceImpl;
    private final ChannelRepository channelRepository;
    private final EntityValidator entityValidator;
    private final ChannelFactory channelFactory;

    public Long addChannel(ChannelCreateCommand command, Member requester) {
        validateDuplicateTitle(command.title());
        Channel channel = channelFactory.createChannel(command, requester);
        entityValidator.validate(channel);
        channelRepository.save(channel);
        channelMemberServiceImpl.joinAsOwner(requester, channel);
        return channel.getId();
    }

    private void validateDuplicateTitle(String title) {
        if(channelRepository.existsByTitle(title)) {
            throw new ChannelAddBadRequestException(ChannelAddBadRequestException.ErrorCode.DUPLICATE_CHANNEL_TITLE);
        }
    }

    public void updateChannelInfo(
            ChannelMember requester,
            ChannelInfoUpdateRequest channelInfoUpdateRequest
    ) {
        Channel channel = requester.getChannel();
        channel.updateInfo(
                requester,
                channelInfoUpdateRequest.title(),
                channelInfoUpdateRequest.description(),
                channelInfoUpdateRequest.profile()
        );
        entityValidator.validate(channel);
    }

    public void deleteChannel(ChannelMember requester) {
        Channel channel = requester.getChannel();
        channel.validateDeletePermission(requester);
        channelRepository.delete(channel);
    }

    public void changeContentOpenLevel(
            ChannelMember requester,
            ChannelContentOpenLevel openLevel
    ) {
        requester.getChannel().updateContentOpenLevel(requester, openLevel);
    }

    public JoinedChannelResponseWrapper createMemberJoinedChannelResponse(Member member) {
        List<JoinedChannelResponse> channels = channelMemberServiceImpl.getChannelMembersByMember(member).stream()
                .map(ChannelMember::getChannel)
                .map(JoinedChannelResponse::from)
                .toList();

        return new JoinedChannelResponseWrapper(channels, channels.size());
    }

    public SearchChannelResponseWrapper createChannelSearchResponse(
            String searchParam,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            ChannelType type,
            ChannelContentOpenLevel contentOpenLevel,
            ChannelJoinLevel joinLevel
    ) {
        Page<ChannelWithMemberCount> channels = channelRepository.searchByKeyword(searchParam, page, size, sort, direction, type, contentOpenLevel, joinLevel);
        return SearchChannelResponseWrapper.from(channels);
    }

    public Channel getChannelByIdOrThrow(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId + "은 존재하지 않는 channelId입니다."));
    }
}
