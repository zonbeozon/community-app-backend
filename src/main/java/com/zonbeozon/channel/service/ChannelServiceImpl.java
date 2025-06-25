package com.zonbeozon.channel.service;

import com.zonbeozon.channel.controller.ChannelUpdateRequest;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAddException;
import com.zonbeozon.channel.exception.ChannelNotFoundException;
import com.zonbeozon.channel.exception.ChannelUpdateException;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.channel.repository.ChannelWithMemberCount;
import com.zonbeozon.channel.repository.JoinedChannelDto;
import com.zonbeozon.channel.service.dto.*;
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
    private final ChannelFactory channelFactory;

    public Long addChannel(ChannelCreateCommand command, Member requester) {
        if(isDuplicateTitle(command.title()))
            throw new ChannelAddException(ChannelAddException.ErrorCode.DUPLICATE_CHANNEL_TITLE);
        Channel channel = channelFactory.createChannel(command, requester);
        channelRepository.save(channel);
        channelMemberServiceImpl.joinAsOwner(requester, channel);
        return channel.getId();
    }

    private boolean isDuplicateTitle(String title) {
        return channelRepository.existsByTitle(title);
    }

    public void updateChannel(
            ChannelMember requester,
            ChannelUpdateRequest request
    ) {
        Channel channel = requester.getChannel();
        if(!channel.hasUpdatePermission(requester))
            throw new ChannelUpdateException(ChannelUpdateException.ErrorCode.ACCESS_DENIED);

        if(!channel.getTitle().equals(request.title())) {
            if(isDuplicateTitle(request.title()))
                throw new ChannelUpdateException(ChannelUpdateException.ErrorCode.DUPLICATE_CHANNEL_TITLE);
            channel.updateTitle(request.title());
        }

        if(!channel.getDescription().equals(request.description())) {
            channel.updateDescription(request.description());
        }

        if(!channel.getProfile().equals(request.profile())) {
            channel.updateProfile(request.profile());
        }

        boolean isSettingChanged =
                channel.getContentOpenLevel() != request.contentOpenLevel()
                || channel.getJoinLevel() != request.joinLevel()
                || channel.getSearchLevel() != request.searchLevel();
        if(isSettingChanged) {
            channel.updateSettings(request.contentOpenLevel(), request.joinLevel(), request.searchLevel());
            if(!channel.isValidSettingCombination())
                throw new ChannelUpdateException(ChannelUpdateException.ErrorCode.INVALID_CHANNEL_SETTING_COMBINATION);
        }
    }

    public void deleteChannel(ChannelMember requester) {
        Channel channel = requester.getChannel();
        channel.validateDeletePermission(requester);
        channelRepository.softDeleteById(channel.getId());
    }

    public JoinedChannelListResponse createMemberJoinedChannelResponse(Member member) {
        List<JoinedChannelDto> channels = channelRepository.findJoinedChannels(member);
        return JoinedChannelListResponse.from(channels);
    }

    public PagedChannelResponse createChannelSearchResponse(
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
        return PagedChannelResponse.from(channels);
    }

    public Channel getChannelByIdOrThrow(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new ChannelNotFoundException(channelId + "은 존재하지 않는 channelId입니다."));
    }
}
