package com.zonbeozon.channel.entity;

import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelInviteCode extends BaseTimeEntity {
    private static final Duration DEFAULT_EXPIRATION = Duration.ofDays(1);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String code;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id")
    private Member inviter;
    @NotNull
    private Long inviteeId;
    @NotNull
    private LocalDateTime expiredAt;

    public static ChannelInviteCode generate(Channel channel, Member inviter, Long inviteeId) {
        ChannelInviteCode inviteCode = new ChannelInviteCode();
        inviteCode.code = UUID.randomUUID().toString();
        inviteCode.channel = channel;
        inviteCode.inviter = inviter;
        inviteCode.inviteeId = inviteeId;
        inviteCode.expiredAt = LocalDateTime.now().plus(DEFAULT_EXPIRATION);
        return inviteCode;
    }

    public boolean isApplicable(Member invitee) {
        return !isExpired() && invitee.getId().equals(inviteeId);
    }

    private boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
