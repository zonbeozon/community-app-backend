package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.exception.*;
import com.zonbeozon.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "channel_type")
@SQLRestriction("is_deleted = false")
public abstract class Channel extends BaseTimeEntity {
    public static final int MIN_TITLE_LENGTH = 2;
    public static final int MAX_TITLE_LENGTH = 30;
    public static final String TITLE_LENGTH_ = "채널 타이틀의 글자수는 2~30자입니다.";
    public static final int MIN_DESCRIPTION_LENGTH = 0;
    public static final int MAX_DESCRIPTION_LENGTH = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
    @Column(unique = true, length = MAX_TITLE_LENGTH)
    private String title;

    @NotNull
    @Size(min = MIN_DESCRIPTION_LENGTH, max = MAX_DESCRIPTION_LENGTH)
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String profile;

    @NotNull
    private boolean isDeleted;

    @NotNull(message = "contentOpenLevel을")
    @Enumerated(EnumType.STRING)
    private ChannelContentOpenLevel contentOpenLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelJoinLevel joinLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelSearchLevel searchLevel;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ChannelType type;


    protected Channel(
            String title,
            String description,
            String profile,
            ChannelContentOpenLevel contentOpenLevel,
            ChannelJoinLevel joinLevel,
            ChannelSearchLevel searchLevel,
            ChannelType type
    ) {
        this.title = title;
        this.description = description;
        this.profile = profile;
        this.contentOpenLevel = contentOpenLevel;
        this.joinLevel = joinLevel;
        this.searchLevel = searchLevel;
        this.type = type;
        this.isDeleted = false;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateProfile(String profile) {
        this.profile = profile;
    }

    public void updateSettings(
            ChannelContentOpenLevel contentOpenLevel,
            ChannelJoinLevel joinLevel,
            ChannelSearchLevel searchLevel
    ) {
        this.contentOpenLevel = contentOpenLevel;
        this.joinLevel = joinLevel;
        this.searchLevel = searchLevel;
    }

    public void kick(ChannelMember requester, ChannelMember target) {
        if(requester.equals(target))
            throw new ChannelBadRequestException(ErrorCode.CANNOT_TARGET_SELF);
        validateKickPermission(requester, target);
        target.updateStatusToKicked();
    }

    public void modifyRole(ChannelMember requester, ChannelMember target, ChannelRole wantTo) {
        validateModifyRolePermission(requester, target, wantTo);
        if(target.getRole() == wantTo)
            throw new ChannelBadRequestException(ErrorCode.SAME_ROLE_CANNOT_BE_UPDATED);
        if(requester.equals(target))
            throw new ChannelBadRequestException(ErrorCode.CANNOT_TARGET_SELF);
        target.updateRole(wantTo);
        //Owner는 채널 당 한명이기 때문에 Owner 권한 이전이 된다.
        if(wantTo == ChannelRole.CHANNEL_OWNER)
            requester.updateRole(ChannelRole.CHANNEL_ADMIN);
    }

    public boolean isValidSettingCombination() {
        //channelSearchLevel이 Private 이지만 ContentOpenLevel은 Public일 수 없다.
        return searchLevel != ChannelSearchLevel.PRIVATE || contentOpenLevel != ChannelContentOpenLevel.PUBLIC;
    }

    public boolean hasUpdatePermission(ChannelMember channelMember) {
        return channelMember.isOwner();
    }

    public void validateDeletePermission(ChannelMember channelMember) {
        if(!channelMember.isOwner())
            throw new ChannelDeleteException(ChannelDeleteException.ErrorCode.ACCESS_DENIED);
    }

    protected void validateKickPermission(ChannelMember requester, ChannelMember target) {
        if(!requester.getRole().isHigherThan(target.getRole()))
            throw new ChannelAccessDeniedException("강퇴시킬려는 맴버보다 권한이 높아야합니다.");
    }

    public void validateInvitePermission(ChannelMember requester) {
        if(!requester.getRole().isHigherThan(ChannelRole.CHANNEL_MEMBER))
            throw new ChannelAccessDeniedException("채널 초대는 Admin이상 부터 할 수 있습니다.");
        if(joinLevel == ChannelJoinLevel.DENY)
            throw new ChannelAccessDeniedException("채널 초대는 해당 채널에서 막힌 상태입니다.");
    }

    public void validateJoinPermission() {
        if(joinLevel != ChannelJoinLevel.OPEN) {
            throw new ChannelAccessDeniedException("공개 가입 채널이 아닙니다");
        }
    }

    public void validateContentReadPermission(ChannelMember channelMember) {
        if (contentOpenLevel == ChannelContentOpenLevel.PUBLIC) {
            return;
        }
        if (contentOpenLevel == ChannelContentOpenLevel.PRIVATE && channelMember == null) {
            throw new ChannelAccessDeniedException("채널 content를 읽을 권한이 없습니다");
        }
    }

    protected void validateModifyRolePermission(ChannelMember requester, ChannelMember target, ChannelRole wantTo) {
        if(!requester.isOwner()) {
            throw new ChannelAccessDeniedException("Owner만 Role을 변경할 수 있습니다.");
        }
    }
}
