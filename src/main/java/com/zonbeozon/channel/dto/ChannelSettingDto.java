package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.ChannelSetting;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;

public record ChannelSettingDto(
        ChannelContentVisibility contentVisibility,
        ChannelJoinPolicy joinPolicy
) {
    public static ChannelSettingDto from(ChannelSetting setting) {
        return new ChannelSettingDto(
                setting.getContentVisibility(),
                setting.getJoinPolicy()
        );
    }
}
