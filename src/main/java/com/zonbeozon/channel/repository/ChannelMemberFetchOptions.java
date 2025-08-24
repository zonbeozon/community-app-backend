package com.zonbeozon.channel.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelMemberFetchOptions {
    private final boolean isWithChannel;
    private final boolean isWithMember;

    public static class Builder {
        private boolean isWithChannel = false;
        private boolean isWithMember = false;

        public ChannelMemberFetchOptions.Builder withMember(boolean isWithMember) {
            this.isWithMember = isWithMember;
            return this;
        }

        public ChannelMemberFetchOptions.Builder withChannel(boolean isWithChannel) {
            this.isWithChannel = isWithChannel;
            return this;
        }

        public ChannelMemberFetchOptions build() {
            return new ChannelMemberFetchOptions(isWithChannel, isWithMember);
        }
    }
}
