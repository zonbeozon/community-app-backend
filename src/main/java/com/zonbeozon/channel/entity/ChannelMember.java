package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.exception.ChannelBadRequestException;
import com.zonbeozon.common.entity.BaseTimeEntity;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@SQLRestriction("status = 'ACTIVE'")
@Slf4j
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
        if(this.role == role) {
            throw new ChannelBadRequestException("변경할려는 Role과 현재 Role이 같습니다");
        }
        this.role = role;
    }

    public void updateStatusToKicked() {
        this.status = ChannelMemberStatus.KICKED;
    }

    public static ChannelMember create(Member member, Channel channel, ChannelRole role) {
        ChannelMember channelMember = new ChannelMember();
        log.info("channelId = {}, memberId = {}", channel.getId(), member.getId());
        channelMember.member = member;
        channelMember.channel = channel;
        channelMember.role = role;
        channelMember.status = ChannelMemberStatus.ACTIVE;
        return channelMember;
    }

    public boolean isOwner() {
        return role == ChannelRole.CHANNEL_OWNER;
    }

    public boolean canLeaveChannel() {
        if(role == ChannelRole.CHANNEL_OWNER) return false;
        return true;
    }
}
