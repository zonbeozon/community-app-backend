package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelProfile;
import com.zonbeozon.image.entity.Image;
import jakarta.persistence.EntityManager;

public class TestChannelProfileBuilder {
    private Channel channel;
    private Image image;

    public TestChannelProfileBuilder(Channel channel, Image image) {
        this.channel = channel;
        this.image = image;
    }

    public ChannelProfile build() {
        return new ChannelProfile(channel, image);
    }
    public ChannelProfile persist(EntityManager entityManager) {
        ChannelProfile channelProfile = build();
        entityManager.persist(channelProfile);
        return channelProfile;
    }
}
