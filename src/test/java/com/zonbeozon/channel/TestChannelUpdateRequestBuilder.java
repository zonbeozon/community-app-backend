package com.zonbeozon.channel;

import com.zonbeozon.channel.dto.ChannelSettingRequest;
import com.zonbeozon.channel.dto.ChannelUpdateRequest;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;

public class TestChannelUpdateRequestBuilder {
    private String title = "example title";
    private String description = "example description";
    private Long imageId;
    private ChannelContentVisibility contentVisibility = ChannelContentVisibility.PUBLIC;
    private ChannelJoinPolicy joinPolicy = ChannelJoinPolicy.OPEN;

    public ChannelUpdateRequest build() {
        return new ChannelUpdateRequest(
                title, description, imageId, new ChannelSettingRequest(contentVisibility, joinPolicy)
        );
    }

    public TestChannelUpdateRequestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TestChannelUpdateRequestBuilder withContentVisibility(ChannelContentVisibility contentVisibility) {
        this.contentVisibility = contentVisibility;
        return this;
    }

    public TestChannelUpdateRequestBuilder withJoinPolicy(ChannelJoinPolicy joinPolicy) {
        this.joinPolicy = joinPolicy;
        return this;
    }

    public TestChannelUpdateRequestBuilder withImageId(Long imageId) {
        this.imageId = imageId;
        return this;
    }
}
