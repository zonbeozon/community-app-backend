package com.zonbeozon.channel.validation;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;

public interface ChannelSettingProvider {
    ChannelContentVisibility contentVisibility();
    ChannelJoinPolicy joinPolicy();
}
