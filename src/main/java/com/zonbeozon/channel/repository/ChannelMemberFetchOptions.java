package com.zonbeozon.channel.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChannelMemberFetchOptions {
    private final boolean isWithChannel;
    private final boolean isWithMember;
    private final boolean isWithMemberProfile;

    public static class Builder {
        private boolean isWithChannel = false;
        private boolean isWithMember = false;
        private boolean isWithMemberProfile = false;

        /**
         * 기본적으로 member profile까지 fetch join됨
         */
        public ChannelMemberFetchOptions.Builder withMember(boolean isWithMember) {
            this.isWithMember = isWithMember;
            this.isWithMemberProfile = isWithMember;
            return this;
        }

        public ChannelMemberFetchOptions.Builder withMemberProfile(boolean isWithMemberProfile) {
            if(isWithMemberProfile) {
                this.isWithMember = true;
                this.isWithMemberProfile = true;
                return this;
            }
            this.isWithMemberProfile = isWithMemberProfile;
            return this;
        }

        public ChannelMemberFetchOptions.Builder withChannel(boolean isWithChannel) {
            this.isWithChannel = isWithChannel;
            return this;
        }

        public ChannelMemberFetchOptions build() {
            return new ChannelMemberFetchOptions(isWithChannel, isWithMember, isWithMemberProfile);
        }
    }
}
