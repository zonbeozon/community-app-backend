package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.JoinedBlogChannelOverview;
import com.zonbeozon.channel.dto.JoinedBlogChannelListResponse;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.repository.BlogChannelRepository;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BlogChannelAssembler {
    private final AuthenticationService authenticationService;
    private final BlogChannelRepository blogChannelRepository;
    private final ChannelMemberFinder channelMemberFinder;

    public JoinedBlogChannelListResponse createJoinedCommunityBlogChannelResponse() {
        Member member = authenticationService.getCurrentMember();
        List<JoinedBlogChannelOverview> joinedChannels = blogChannelRepository.getBlogChannelsByMember(member, ChannelCreatorType.COMMUNITY);
        return JoinedBlogChannelListResponse.from(joinedChannels);
    }
}
