package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.InfoChannelOverview;
import com.zonbeozon.channel.dto.JoinedInfoChannelListResponse;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.repository.InfoChannelRepository;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class InfoChannelAssembler {
    private final AuthenticationService authenticationService;
    private final InfoChannelRepository infoChannelRepository;

    public JoinedInfoChannelListResponse createJoinedCommunityInfoChannelResponse() {
        Member member = authenticationService.getCurrentMember();
        List<InfoChannelOverview> joinedChannels = infoChannelRepository.getInfoChannelsByMember(member, ChannelCreatorType.COMMUNITY);
        return JoinedInfoChannelListResponse.from(joinedChannels);
    }
}
