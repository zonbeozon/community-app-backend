package com.zonbeozon.channel.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChannelRole {
    CHANNEL_OWNER(2),
    CHANNEL_ADMIN(1),
    CHANNEL_MEMBER(0);

    private final int level;

    public boolean isHigherThan(ChannelRole role) {
        return this.level > role.level;
    }

    public boolean isEqual(ChannelRole other) {
        return this.level == other.level;
    }
}
