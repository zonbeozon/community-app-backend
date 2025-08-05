package com.zonbeozon.channel.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.ChannelMemberCount;
import com.zonbeozon.channel.dto.JoinedBlogChannelOverview;
import com.zonbeozon.channel.dto.JoinedBlogChannelListResponse;
import com.zonbeozon.channel.dto.JoinedBlogChannelResponse;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.repository.BlogChannelRepository;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.dto.PostImageCount;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BlogChannelAssembler {
    private final AuthenticationService authenticationService;
    private final BlogChannelRepository blogChannelRepository;
    private final PostImageRepository postImageRepository;
    private final ChannelMemberRepository channelMemberRepository;

    public JoinedBlogChannelListResponse createJoinedCommunityBlogChannelResponse() {
        Member member = authenticationService.getCurrentMember();
        List<JoinedBlogChannelOverview> joinedChannels = blogChannelRepository.getBlogChannelsByMember(member, ChannelCreatorType.COMMUNITY);
        List<Long> postIds = joinedChannels.stream()
                .map(JoinedBlogChannelOverview::getLatestPost)
                .filter(Objects::nonNull)
                .map(Post::getId)
                .toList();
        Map<Long, Long> postImageCount = postImageRepository.countImagesByPostIds(postIds).stream()
                .collect(Collectors.toMap(PostImageCount::getPostId, PostImageCount::getCount));

        List<Long> channelIds = joinedChannels.stream()
                .map(JoinedBlogChannelOverview::getBlogChannel)
                .map(BlogChannel::getId)
                .toList();
        Map<Long, Long> channelMemberCounts = channelMemberRepository.countChannelMemberByChannelIds(channelIds).stream()
                .collect(Collectors.toMap(ChannelMemberCount::getChannelId, ChannelMemberCount::getCount));

        List<JoinedBlogChannelResponse> responses = joinedChannels.stream().map(
                overview -> JoinedBlogChannelResponse.from(
                        member,
                        overview.getRequesterRole(),
                        overview.getBlogChannel(),
                        overview.getProfile(),
                        channelMemberCounts.get(overview.getBlogChannel().getId()),
                        overview.getLatestPost(),
                        overview.getLatestPostAuthorRole(),
                        overview.getLatestPostAuthor(),
                        overview.getLatestPost() == null ? null : postImageCount.get(overview.getLatestPost().getId()))
        ).toList();
        return JoinedBlogChannelListResponse.from(responses);
    }
}
