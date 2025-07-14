package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelSearchScope;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelSearchScope searchScope;

    public void updateSettings(
            ChannelContentVisibility contentVisibility,
            ChannelJoinPolicy joinLevel,
            ChannelSearchScope searchLevel
    ) {
        this.contentVisibility = contentVisibility;
        this.joinPolicy = joinLevel;
        this.searchScope = searchLevel;
    }
}
