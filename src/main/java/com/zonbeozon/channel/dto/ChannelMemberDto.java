package com.zonbeozon.channel.dto;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import org.springframework.lang.Nullable;

public record ChannelMemberDto(
        Long memberId,
        String username,
        @Nullable ImageDto profile,
        ServerRole serverRole,
        ChannelRole channelRole
) {
    public ChannelMemberDto(Long memberId, String username, @Nullable Long imageId, @Nullable String imageUrl, ServerRole serverRole, ChannelRole channelRole) {
        this(
                memberId,
                username,
                imageId == null ? null : new ImageDto(imageId, imageUrl),
                serverRole,
                channelRole
        );
    }

    public static ChannelMemberDto from(ChannelMember channelMember) {
        Member member = channelMember.getMember();
        return new ChannelMemberDto(
                member.getId(),
                member.getUsername(),
                ImageDto.create(member.getProfile().getImage()),
                member.getRole(),
                channelMember.getRole()
        );
    }
}
