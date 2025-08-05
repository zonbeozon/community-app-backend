package com.zonbeozon.post.dto;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostWithAuthor {
    private Post post;
    private ChannelMember author;
}
