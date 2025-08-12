package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Slf4j
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "channel_member_uk",
                        columnNames = {"channel_id", "member_id"}
                )
        },
        indexes = {
                @Index(name = "idx_channel_member_member_id_channel_id", columnList = "member_id, channel_id")
        }
)
@SQLRestriction("status = 'ACTIVE'")
public class ChannelMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelRole role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelMemberStatus status;

    public void updateRole(ChannelRole role) {
        this.role = role;
    }

    public void updateStatusToKicked() {
        this.status = ChannelMemberStatus.KICKED;
    }

    public static ChannelMember create(Member member, Channel channel, ChannelRole role, ChannelMemberStatus status) {
        ChannelMember channelMember = new ChannelMember();
        channelMember.member = member;
        channelMember.channel = channel;
        channelMember.role = role;
        channelMember.status = status;
        return channelMember;
    }

    public boolean isOwner() {
        return role == ChannelRole.CHANNEL_OWNER;
    }

    public boolean canLeaveChannel() {
        return role != ChannelRole.CHANNEL_OWNER;
    }
}
