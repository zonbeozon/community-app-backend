package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelSearchScope;

public record ChannelSettingResponse(
        ChannelContentVisibility contentVisibility,
        ChannelJoinPolicy joinPolicy,
        ChannelSearchScope searchScope

) {
    public static ChannelSettingResponse from(ChannelSetting setting) {
        return new ChannelSettingResponse(
                setting.getContentVisibility(),
                setting.getJoinPolicy(),
                setting.getSearchScope()
        );
    }
}
