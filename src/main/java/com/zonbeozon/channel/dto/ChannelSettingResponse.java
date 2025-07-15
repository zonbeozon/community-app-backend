package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.enums.ChannelVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;

public record ChannelSettingResponse(
        ChannelVisibility contentVisibility,
        ChannelJoinPolicy joinPolicy

) {
    public static ChannelSettingResponse from(ChannelSetting setting) {
        return new ChannelSettingResponse(
                setting.getVisibility(),
                setting.getJoinPolicy()
        );
    }
}
