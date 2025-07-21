package com.zonbeozon.post.entity;

import com.zonbeozon.image.entity.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post_image SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @NotNull
    private int displayOrder;

    private boolean isDeleted = false;

    public PostImage(Post post, Image image, int displayOrder) {
        this.post = post;
        this.image = image;
        this.displayOrder = displayOrder;
    }
}
