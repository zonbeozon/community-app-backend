package com.zonbeozon.post;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TestPostBuilder {
    private String content= "test content";
    private BlogChannel channel;
    private Member member;

    public TestPostBuilder(BlogChannel blogChannel, Member member) {
        this.channel = blogChannel;
        this.member = member;
    }

    public Post build() {
        return Post.create(content, channel, member);
    }

    public Post persist(EntityManager entityManager) {
        Post post = build();
        entityManager.persist(post);
        return post;
    }
}
