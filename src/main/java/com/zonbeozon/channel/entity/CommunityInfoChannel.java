package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.service.ChannelCreateCommand;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("COMMUNITY_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityInfoChannel extends Channel {

    private CommunityInfoChannel(ChannelCreateCommand command) {
        super(
                command.title(),
                command.description(),
                command.profile(),
                command.contentOpenLevel(),
                command.joinLevel(),
                command.searchLevel()
        );
    }

    public static CommunityInfoChannel create(ChannelCreateCommand command, Member member) {
        return new CommunityInfoChannel(command);
    }
}
