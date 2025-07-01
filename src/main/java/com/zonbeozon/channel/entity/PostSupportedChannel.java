package com.zonbeozon.channel.entity;

import com.zonbeozon.channel.service.ChannelCreateCommand;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.exception.PostAccessDeniedException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("POST_SUPPORTED_CHANNEL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSupportedChannel extends Channel {

    protected PostSupportedChannel(ChannelCreateCommand command) {
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

    /**
     * 기본 값은 admin래밸 부터 post 작성가능
     */
    public void validatePostCreation(ChannelMember channelMember) {
        if(!channelMember.getRole().isHigherThan(ChannelRole.CHANNEL_MEMBER))
            throw new PostAccessDeniedException(PostAccessDeniedException.ErrorCode.POST_CREATION_DENIED);
    }

    /**
     * 작성자나 작성자보다 높은 권한을 가진 채널 맴버만 게시글을 삭제할 수 있다.
     */
    public void validatePostDeletePermission(Post post, ChannelMember channelMember) {
        validateChannelMemberMatch(channelMember);
        if (!(post.isAuthor(channelMember) || channelMember.getRole().isHigherThan(channelMember.getRole())))
            throw new PostAccessDeniedException(PostAccessDeniedException.ErrorCode.POST_DELETION_DENIED);
    }

    public void validatePostUpdatePermission(Post post, ChannelMember channelMember) {
        validateChannelMemberMatch(channelMember);
        if(!post.isAuthor(channelMember)) {
            throw new PostAccessDeniedException(PostAccessDeniedException.ErrorCode.POST_UPDATE_DENIED);
        }
    }
}
