package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.channel.dto.ChannelInfoDto;
import com.zonbeozon.channel.dto.ChannelMemberDto;
import com.zonbeozon.channel.dto.ChannelViewDto;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelViewAssembler {
    private final ChannelRepository channelRepository;
    private final ChannelInfoAssembler channelInfoAssembler;
    private final ChannelMemberRepository channelMemberRepository;

    public ChannelViewDto getChannelView(Long channelId, Long requesterId) {
        ChannelInfoDto channelInfo = channelInfoAssembler.getChannelInfo(channelId);
        Optional<ChannelMemberDto> optChannelMember = channelMemberRepository.findChannelMemberDtoByChannelIdAndMemberId(channelId, requesterId);
        return optChannelMember
                .map(channelMemberDto -> new ChannelViewDto(channelInfo, true, channelMemberDto))
                .orElseGet(() -> new ChannelViewDto(channelInfo, false, null));
    }
}
