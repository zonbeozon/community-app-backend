package com.zonbeozon.channel;

import com.zonbeozon.channel.dto.ChannelCreateRequest;
import com.zonbeozon.channel.dto.ChannelSettingRequest;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;

public class TestChannelCreateRequestBuilder {
    private String title = "example title";
    private String description = "example description";
    private Long imageId;
    private ChannelContentVisibility visibility = ChannelContentVisibility.PUBLIC;
    private ChannelType channelType = ChannelType.BLOG;
    private ChannelJoinPolicy joinPolicy = ChannelJoinPolicy.OPEN;

    public ChannelCreateRequest build() {
        return new ChannelCreateRequest(
                channelType, title, description, imageId, new ChannelSettingRequest(visibility, joinPolicy)
        );
    }

    public TestChannelCreateRequestBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TestChannelCreateRequestBuilder setContentVisibility(ChannelContentVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public TestChannelCreateRequestBuilder setJoinPolicy(ChannelJoinPolicy joinPolicy) {
        this.joinPolicy = joinPolicy;
        return this;
    }

    public TestChannelCreateRequestBuilder setImageId(Long imageId) {
        this.imageId = imageId;
        return this;
    }
}
