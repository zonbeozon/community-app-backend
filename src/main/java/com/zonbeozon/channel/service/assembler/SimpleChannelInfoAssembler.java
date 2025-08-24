package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.ChannelInfoResponse;
import com.zonbeozon.channel.dto.ChannelWithMemberCount;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 데이터 베이스로 부터 DTO 생성
 */
@Component
@RequiredArgsConstructor
public class SimpleChannelInfoAssembler implements ChannelInfoAssembler {
    private final ChannelRepository channelRepository;

    @Override
    public ChannelInfoResponse getChannelInfo(Long channelId) {
        ChannelWithMemberCount channelWithMemberCount = channelRepository.findByIdWithProfileAndMemberCount(channelId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
        return ChannelInfoResponse.from(channelWithMemberCount);
    }

    @Override
    public List<ChannelInfoResponse> getChannelInfos(List<Long> channelIds) {
        List<ChannelWithMemberCount> channelWithMemberCountList = channelRepository.findByIdInWithProfileAndMemberCount(channelIds);
        if(channelWithMemberCountList.size() != channelIds.size()) throw new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
        return channelWithMemberCountList.stream().map(ChannelInfoResponse::from).toList();
    }
}
