package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.post.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BlogChannelOverview {
    private BlogChannel blogChannel;
    private Long memberCount;
    private Post latestPost;
}
