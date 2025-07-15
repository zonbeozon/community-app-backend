package com.zonbeozon.channel.validation;

import com.zonbeozon.channel.enums.ChannelVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;

public interface ChannelSettingProvider {
    ChannelVisibility visibility();
    ChannelJoinPolicy joinPolicy();
}
