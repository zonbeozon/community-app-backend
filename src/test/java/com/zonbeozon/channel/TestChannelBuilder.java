package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.entity.ChatChannel;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;
import jakarta.persistence.EntityManager;

public class TestChannelBuilder {
    private String title = "example title";
    private String description = "example description";
    private ChannelContentVisibility visibility = ChannelContentVisibility.PUBLIC;
    private ChannelJoinPolicy joinPolicy = ChannelJoinPolicy.OPEN;
    private ChannelCreatorType creatorType = ChannelCreatorType.COMMUNITY;
    private ChannelType channelType = ChannelType.BLOG;

    public Channel build() {
        return switch (channelType) {
            case BLOG:
                yield new BlogChannel(title, description, new ChannelSetting(visibility, joinPolicy), creatorType);

            case CHAT:
                yield new ChatChannel(title, description, new ChannelSetting(visibility, joinPolicy), creatorType);
        };
    }

    public Channel persist(EntityManager entityManager) {
        Channel channel = build();
        entityManager.persist(channel);
        return channel;
    }

    public TestChannelBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TestChannelBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TestChannelBuilder withType(ChannelType type) {
        this.channelType = type;
        return this;
    }
}
