package com.zonbeozon.channel.dto;

import com.zonbeozon.post.dto.PostResponse;

public record JoinedBlogChannelInfoResponse(
    ChannelInfoResponse channelInfo,
    PostResponse latestPost,
    ChannelMemberResponse requester
) {
}
