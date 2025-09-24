package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.ChannelInfoDto;
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
    public ChannelInfoDto getChannelInfo(Long channelId) {
        return channelRepository.findByIdWithProfileAndChannelMemberCount(channelId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }

    @Override
    public List<ChannelInfoDto> getChannelInfos(List<Long> channelIds) {
        List<ChannelInfoDto> channelInfos = channelRepository.findByIdInWithProfileAndChannelMemberCount(channelIds);
        if(channelInfos.size() != channelIds.size()) throw new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
        return channelInfos;
    }
}
