package com.zonbeozon.image;

import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;

public class TestMockImageBuilder {
    private Member member;
    private String key;
    private String url;

    public TestMockImageBuilder(Member member, String key) {
        this.member = member;
        this.key = key;
        this.url = "test.com/" + key;
    }

    public Image build() {
        return new Image(url, key, member);
    }

    public Image persist(EntityManager entityManager) {
        Image image = build();
        entityManager.persist(image);
        return image;
    }
}
