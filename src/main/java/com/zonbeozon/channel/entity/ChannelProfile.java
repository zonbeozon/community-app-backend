package com.zonbeozon.channel.entity;

import com.zonbeozon.image.entity.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChannelProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    private boolean isDeleted = false;

    public ChannelProfile(Channel channel, Image image) {
        this.channel = channel;
        this.image = image;
    }

    public void updateImage(Image image) {
        this.image = image;
    }
}
