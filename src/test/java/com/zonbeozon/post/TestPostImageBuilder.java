package com.zonbeozon.post;

import com.zonbeozon.image.entity.Image;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.entity.PostImage;
import jakarta.persistence.EntityManager;

public class TestPostImageBuilder {
    private Post post;
    private Image image;
    private int displayOrder;

    public TestPostImageBuilder(Post post, Image image, int displayOrder) {
        this.post = post;
        this.image = image;
        this.displayOrder = displayOrder;
    }

    public PostImage build() {
        return new PostImage(post, image, displayOrder);
    }
    public PostImage persist(EntityManager entityManager) {
        PostImage postImage = build();
        entityManager.persist(postImage);
        return postImage;
    }
}
