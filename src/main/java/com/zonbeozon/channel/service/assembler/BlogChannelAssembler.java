package com.zonbeozon.channel.service.assembler;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.*;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.repository.BlogChannelRepository;
import com.zonbeozon.post.dto.PostResponse;
import com.zonbeozon.post.service.PostAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BlogChannelAssembler {
    private final BlogChannelRepository blogChannelRepository;
    private final ChannelInfoAssembler channelInfoAssembler;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final PostAssembler postAssembler;

    public JoinedBlogChannelInfoListResponse getJoinedCommunityBlogChannelInfo(Long memberId) {
        List<BlogChannel> joinedBlogChannels = blogChannelRepository.findAllByMemberId(memberId);
        List<Long> joinBlogChannelIds = joinedBlogChannels.stream().map(BlogChannel::getId).toList();
        Map<Long, ChannelInfoResponse> channelInfos = channelInfoAssembler.getChannelInfos(joinBlogChannelIds).stream()
                .collect(Collectors.toMap(ChannelInfoResponse::channelId, Function.identity()));
        //requester info
        Map<ChannelMemberId, ChannelMemberResponse> requesterInfos = channelMemberAssembler.getChannelMemberResponse(
                joinBlogChannelIds.stream().map(channelId  -> new ChannelMemberId(channelId, memberId)).toList()
        );
        //latestPost
        List<Long> latestPostIds = joinedBlogChannels.stream().map(BlogChannel::getLatestPostId).filter(Objects::nonNull).toList();
        Map<Long, PostResponse> postResponses = postAssembler.getPostResponses(latestPostIds);

        List<JoinedBlogChannelInfoResponse> joinedBlogChannelInfos = joinedBlogChannels.stream()
                .sorted(Comparator.comparing(
                        (BlogChannel channel) -> {
                            PostResponse latestPost = postResponses.get(channel.getLatestPostId());
                            return (latestPost != null) ? latestPost.createdAt() : LocalDateTime.MIN;
                        },
                        Comparator.reverseOrder()
                ))
                .map(channel -> {
                    Long channelId = channel.getId();
                    ChannelInfoResponse channelInfo = channelInfos.get(channelId);
                    ChannelMemberResponse requesterInfo = requesterInfos.get(new ChannelMemberId(channelId, memberId));
                    PostResponse latestPostInfo = postResponses.get(channel.getLatestPostId());

                    return new JoinedBlogChannelInfoResponse(channelInfo, latestPostInfo, requesterInfo);
                })
                .toList();

        return JoinedBlogChannelInfoListResponse.from(joinedBlogChannelInfos);
    }
}
