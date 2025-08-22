package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;

public record ChannelSettingResponse(
        ChannelContentVisibility visibility,
        ChannelJoinPolicy joinPolicy

) {
    public static ChannelSettingResponse from(ChannelSetting setting) {
        return new ChannelSettingResponse(
                setting.getContentVisibility(),
                setting.getJoinPolicy()
        );
    }
}
