package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.exception.ChannelBadRequestException;
import com.zonbeozon.channel.service.ChannelCreateCommand;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("OFFICIAL_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OficialInfoChannel extends Channel {

    private OficialInfoChannel(ChannelCreateCommand command) {
        super(
                command.title(),
                command.description(),
                command.profile(),
                command.contentOpenLevel(),
                command.joinLevel(),
                command.searchLevel(),
                command.type()
        );
    }

    public static OficialInfoChannel create(ChannelCreateCommand command, Member member) {
        if (!member.isAdmin()) throw new ChannelBadRequestException("어드민만 OfficialInfoChannel를 생성할 수 있습니다.");
        return new OficialInfoChannel(command);
    }
}
