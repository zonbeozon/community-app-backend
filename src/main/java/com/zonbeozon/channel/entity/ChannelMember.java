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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_channel_member",
                        columnNames = {"channel_id", "member_id"}
                )
        }
)
public class ChannelMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelRole role;

    public void updateRole(ChannelRole role) {
        this.role = role;
    }


    public static ChannelMember create(Member member, Channel channel, ChannelRole role) {
        ChannelMember channelMember = new ChannelMember();
        channelMember.member = member;
        channelMember.channel = channel;
        channelMember.role = role;
        return channelMember;
    }

    public boolean isOwner() {
        return role == ChannelRole.CHANNEL_OWNER;
    }

    public boolean canLeaveChannel() {
        return role != ChannelRole.CHANNEL_OWNER;
    }
}
