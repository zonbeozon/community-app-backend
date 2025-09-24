package com.zonbeozon.channel.dto;

import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.domain.ServerRole;
import org.springframework.lang.Nullable;

public record BannedChannelMemberDto(
    Long memberId,
    String username,
    @Nullable
    ImageDto profile,
    ServerRole serverRole,
    @Nullable
    String reason
) {
    public BannedChannelMemberDto(
            Long memberId,
            String username,
            Long imageId,
            String imageUrl,
            ServerRole serverRole,
            String reason
    ) {
        this(memberId, username, imageId == null ? null : new ImageDto(imageId, imageUrl) , serverRole, reason);
    }
}
