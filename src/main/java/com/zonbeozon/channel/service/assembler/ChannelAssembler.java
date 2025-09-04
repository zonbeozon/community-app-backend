package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.*;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ChannelAssembler {
    private final ChannelInfoAssembler channelInfoAssembler;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final ChannelRepository channelRepository;

    public ChannelInfosWithRequesterResponse getJoinedChannelInfosWithRequester(Long requesterId) {
        List<Channel> joinedChannels = channelRepository.findAllByMemberIdOrderByLatestEventOccurred(requesterId);
        return getChannelInfosWithRequester(joinedChannels, requesterId);
    }

    public ChannelInfoWithRequesterResponse getJoinedChannelInfoWithRequester(Long requesterId, Long channelId) {
        return new ChannelInfoWithRequesterResponse(
                channelInfoAssembler.getChannelInfo(channelId),
                channelMemberAssembler.getChannelMemberResponse(new ChannelMemberId(requesterId, channelId))
        );
    }

    private ChannelInfosWithRequesterResponse getChannelInfosWithRequester(List<Channel> channels, Long requesterId) {
        List<Long> joinChannelIds = channels.stream().map(Channel::getId).toList();
        //channel info
        Map<Long, ChannelInfoResponse> channelInfos = channelInfoAssembler.getChannelInfos(joinChannelIds).stream()
                .collect(Collectors.toMap(ChannelInfoResponse::channelId, Function.identity()));
        //requester info
        Map<ChannelMemberId, ChannelMemberResponse> requesterInfos = channelMemberAssembler.getChannelMemberResponse(
                joinChannelIds.stream().map(channelId  -> new ChannelMemberId(channelId, requesterId)).toList(),
                false
        );

        List<ChannelInfoWithRequesterResponse> joinedChannelInfosWithRequester = channels.stream()
                .map(channel -> {
                    Long channelId = channel.getId();
                    ChannelInfoResponse channelInfo = channelInfos.get(channelId);
                    ChannelMemberResponse requesterInfo = requesterInfos.get(new ChannelMemberId(channelId, requesterId));
                    return new ChannelInfoWithRequesterResponse(channelInfo, requesterInfo);
                })
                .toList();

        return ChannelInfosWithRequesterResponse.from(joinedChannelInfosWithRequester);
    }
}
