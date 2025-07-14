package com.zonbeozon.channel.validation;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelSearchScope;

public interface ChannelSettingProvider {
    ChannelContentVisibility contentVisibility();
    ChannelJoinPolicy joinPolicy();
    ChannelSearchScope searchScope();
}
