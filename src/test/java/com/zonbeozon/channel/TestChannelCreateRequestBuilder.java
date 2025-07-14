package com.zonbeozon.channel;

import com.zonbeozon.channel.dto.ChannelCreateRequest;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelSearchScope;
import com.zonbeozon.channel.enums.ChannelType;

public class TestChannelCreateRequestBuilder {
    private String title = "example title";
    private String description = "example description";
    private String profile = "exampleProfile";
    private ChannelContentVisibility contentVisibility = ChannelContentVisibility.PUBLIC;
    private ChannelType channelType = ChannelType.INFO;
    private ChannelJoinPolicy joinPolicy = ChannelJoinPolicy.OPEN;
    private ChannelSearchScope searchScope = ChannelSearchScope.PUBLIC;

    public ChannelCreateRequest build() {
        return new ChannelCreateRequest(
                title, description, profile, contentVisibility, channelType, joinPolicy, searchScope
        );
    }

    public TestChannelCreateRequestBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TestChannelCreateRequestBuilder setContentVisibility(ChannelContentVisibility contentVisibility) {
        this.contentVisibility = contentVisibility;
        return this;
    }

    public TestChannelCreateRequestBuilder setSearchScope(ChannelSearchScope searchScope) {
        this.searchScope = searchScope;
        return this;
    }

    public TestChannelCreateRequestBuilder setJoinPolicy(ChannelJoinPolicy joinPolicy) {
        this.joinPolicy = joinPolicy;
        return this;
    }
}
