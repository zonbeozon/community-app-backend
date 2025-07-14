package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ChannelMemberAssembler {
    private final ChannelMemberRepository channelMemberRepository;

    public List<ChannelMemberResponse> createChannelMemberListResponse(List<Member> members, Channel channel) {
        return channelMemberRepository.findByChannelAndMemberIn(channel, members).stream()
                .map(ChannelMemberResponse::from)
                .toList();
    }
}
