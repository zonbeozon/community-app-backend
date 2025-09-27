package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.ChannelInfoWithMembershipDto;
import com.zonbeozon.channel.dto.ChannelInfosWithMembershipDto;
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

    public ChannelInfosWithMembershipDto getJoinedChannels(Long memberId) {
        List<ChannelInfoWithMembershipDto> channelInfosWithRequester = channelMemberRepository.findChannelInfoWithRequesterByMemberIdOrderByLatestEventOccurredDesc(memberId);
        return new ChannelInfosWithMembershipDto(channelInfosWithRequester, channelInfosWithRequester.size());
    }

    public ChannelInfoWithMembershipDto getJoinedChannel(Long channelId, Long memberId) {
        return channelMemberRepository.findChannelInfoWithRequesterByChannelIdAndMemberId(channelId, memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    }
}
