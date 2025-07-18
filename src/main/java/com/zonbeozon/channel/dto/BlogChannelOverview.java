package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.post.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BlogChannelOverview {
    private final BlogChannel blogChannel;
    private final Long memberCount;
    private final Post latestPost;
}
