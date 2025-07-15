package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelVisibility;
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
    private ChannelVisibility visibility;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelJoinPolicy joinPolicy;

    public void updateSettings(
            ChannelVisibility visibility,
            ChannelJoinPolicy joinLevel
    ) {
        this.visibility = visibility;
        this.joinPolicy = joinLevel;
    }
}
