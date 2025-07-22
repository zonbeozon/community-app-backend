package com.zonbeozon.channel;

import com.zonbeozon.channel.dto.ChannelCreateRequest;
import com.zonbeozon.channel.dto.ChannelSettingRequest;
import com.zonbeozon.channel.dto.ChannelUpdateRequest;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.channel.enums.ChannelVisibility;

public class TestChannelUpdateRequestBuilder {
    private String title = "example title";
    private String description = "example description";
    private Long imageId;
    private ChannelVisibility visibility = ChannelVisibility.PUBLIC;
    private ChannelJoinPolicy joinPolicy = ChannelJoinPolicy.OPEN;

    public ChannelUpdateRequest build() {
        return new ChannelUpdateRequest(
                title, description, imageId, new ChannelSettingRequest(visibility, joinPolicy)
        );
    }

    public TestChannelUpdateRequestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TestChannelUpdateRequestBuilder withContentVisibility(ChannelVisibility visibility) {
        this.visibility = visibility;
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
