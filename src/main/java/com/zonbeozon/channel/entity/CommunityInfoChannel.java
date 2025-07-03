package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.service.ChannelAddCommand;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("COMMUNITY_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityInfoChannel extends PostSupportedChannel {

    private CommunityInfoChannel(ChannelAddCommand command) {
        super(command);
    }

    public static CommunityInfoChannel create(ChannelAddCommand command, Member member) {
        return new CommunityInfoChannel(command);
    }
}
