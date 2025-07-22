package com.zonbeozon.post;

import com.zonbeozon.image.entity.Image;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import jakarta.persistence.EntityManager;

public class TestPostImageBuilder {
    private Post post;
    private Image image;

    public TestPostImageBuilder(Post post, Image image) {
        this.post = post;
        this.image = image;
    }

    public PostImage build() {
        return new PostImage(post, image);
    }
    public PostImage persist(EntityManager entityManager) {
        PostImage postImage = build();
        entityManager.persist(postImage);
        return postImage;
    }
}
