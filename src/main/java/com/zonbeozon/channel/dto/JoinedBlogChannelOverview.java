package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinedBlogChannelOverview {
    private BlogChannel blogChannel;
    private Image profile;
    private Post latestPost;
    private ChannelRole latestPostAuthorRole;
    private Member latestPostAuthor;

    private Long memberCount;
    private int latestPostImageCount;

    public JoinedBlogChannelOverview(
            BlogChannel blogChannel,
            Image profile,
            Post latestPost,
            ChannelRole latestPostAuthorRole,
            Member latestPostAuthor
    ) {
        this.blogChannel = blogChannel;
        this.profile = profile;
        this.latestPost = latestPost;
        this.latestPostAuthorRole = latestPostAuthorRole;
        this.latestPostAuthor = latestPostAuthor;
    }
}
