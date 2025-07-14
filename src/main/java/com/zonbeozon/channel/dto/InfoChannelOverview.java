package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.InfoChannel;
import com.zonbeozon.post.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InfoChannelOverview {
    private InfoChannel infoChannel;
    private Long memberCount;
    private Post latestPost;
}
