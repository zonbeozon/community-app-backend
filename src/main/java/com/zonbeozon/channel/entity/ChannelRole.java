package com.zonbeozon.channel.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ChannelRole {
    CHANNEL_OWNER(2),
    CHANNEL_ADMIN(1),
    CHANNEL_MEMBER(0);

    private final int level;

    public boolean isHigherThan(ChannelRole other) {
        return this.level > other.level;
    }

    public static boolean isPromote(ChannelRole from, ChannelRole to) {
        return from.level < to.level;
    }
}
