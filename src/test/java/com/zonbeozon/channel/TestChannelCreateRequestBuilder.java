package com.zonbeozon.channel;

import com.zonbeozon.channel.dto.ChannelCreateRequest;
import com.zonbeozon.channel.dto.ChannelSettingRequest;
import com.zonbeozon.channel.enums.ChannelVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;

public class TestChannelCreateRequestBuilder {
    private String title = "example title";
    private String description = "example description";
    private String profile = "exampleProfile";
    private ChannelVisibility visibility = ChannelVisibility.PUBLIC;
    private ChannelType channelType = ChannelType.BLOG;
    private ChannelJoinPolicy joinPolicy = ChannelJoinPolicy.OPEN;

    public ChannelCreateRequest build() {
        return new ChannelCreateRequest(
                channelType, title, description, profile, new ChannelSettingRequest(visibility, joinPolicy)
        );
    }

    public TestChannelCreateRequestBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TestChannelCreateRequestBuilder setContentVisibility(ChannelVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public TestChannelCreateRequestBuilder setJoinPolicy(ChannelJoinPolicy joinPolicy) {
        this.joinPolicy = joinPolicy;
        return this;
    }
}
