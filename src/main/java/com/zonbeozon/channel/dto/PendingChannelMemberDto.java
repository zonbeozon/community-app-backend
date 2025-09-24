package com.zonbeozon.channel.dto;

import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.domain.ServerRole;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public record PendingChannelMemberDto(
        Long memberId,
        String username,
        @Nullable
        ImageDto profile,
        ServerRole serverRole,
        LocalDateTime requestedAt
) {
        public PendingChannelMemberDto(
                Long memberId,
                String username,
                @Nullable Long imageId,
                @Nullable String imageUrl,
                ServerRole serverRole,
                LocalDateTime requestedAt) {
                this(memberId, username, imageId == null ? null : new ImageDto(imageId, imageUrl), serverRole, requestedAt);
        }
}
