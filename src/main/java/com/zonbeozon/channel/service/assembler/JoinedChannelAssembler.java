package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.ChannelInfoWithRequesterDto;
import com.zonbeozon.channel.dto.ChannelInfosWithRequesterDto;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinedChannelAssembler {
    private final ChannelMemberRepository channelMemberRepository;

    public ChannelInfosWithRequesterDto getJoinedChannels(Long memberId) {
        List<ChannelInfoWithRequesterDto> channelInfosWithRequester = channelMemberRepository.findChannelInfoWithRequesterByMemberIdOrderByLatestEventOccurredDesc(memberId);
        return new ChannelInfosWithRequesterDto(channelInfosWithRequester, channelInfosWithRequester.size());
    }

    public ChannelInfoWithRequesterDto getJoinedChannel(Long channelId, Long memberId) {
        return channelMemberRepository.findChannelInfoWithRequesterByChannelIdAndMemberId(channelId, memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }
}
