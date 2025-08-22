package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelSetting {
    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelContentVisibility contentVisibility;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelJoinPolicy joinPolicy;

    public void updateSettings(
            ChannelContentVisibility visibility,
            ChannelJoinPolicy joinLevel
    ) {
        this.contentVisibility = visibility;
        this.joinPolicy = joinLevel;
    }
}
