package com.zonbeozon.post.entity;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.global.ChannelMemberFixture;
import com.zonbeozon.global.PostFixture;
import com.zonbeozon.post.exception.PostAccessDeniedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PostTest {
    @DisplayName("삭제 권한 테스트")
    @Nested
    class DeletePermissionTest {
        @DisplayName("다른 채널 맴버는 삭제가 불가능하다.")
        @Test
        void shouldDenyDeletionWhenMemberIsFromDifferentChannel() {
            Post postCreateAtChannelA = PostFixture.ChannelA.post_1;
            ChannelMember otherchannelMember = ChannelMemberFixture.ChannelB.OwnerRole.member_1;
            Assertions.assertThatThrownBy(() -> postCreateAtChannelA.validateDeletePermission(otherchannelMember))
                    .isInstanceOf(PostAccessDeniedException.class);
        }

        @DisplayName("작성자보다 권한이 높다면 삭제 가능하다.")
        @Test
        void shouldAllowDeletionWhenMemberHasHigherRoleThanAuthor() {
            Post postCreatedByMemberRole = PostFixture.ChannelA.post_2;
            ChannelMember higherPrivilegeChannelMember = ChannelMemberFixture.ChannelA.OwnerRole.member_1;
            postCreatedByMemberRole.validateDeletePermission(higherPrivilegeChannelMember);
        }

        @DisplayName("작성자와 권한이 같다면 삭제 불가능하다.")
        @Test
        void shouldDenyDeletionWhenMemberHasSameRoleAsAuthor() {
            Post postCreatedByOwnerRole = PostFixture.ChannelA.post_1;
            ChannelMember samePrivilegeChannelMember = ChannelMemberFixture.ChannelA.MemberRole.member_3;
            Assertions.assertThatThrownBy(() -> postCreatedByOwnerRole.validateDeletePermission(samePrivilegeChannelMember))
                .isInstanceOf(PostAccessDeniedException.class);
        }

        @DisplayName("작성자보다 권한이 낮다면 삭제 불가능하다.")
        @Test
        void shouldDenyDeletionWhenMemberHasLowerRoleThanAuthor() {
            Post postCreatedByOwnerRole = PostFixture.ChannelA.post_1;
            ChannelMember lowerPrivilegeChannelMember = ChannelMemberFixture.ChannelA.MemberRole.member_3;
            Assertions.assertThatThrownBy(() -> postCreatedByOwnerRole.validateDeletePermission(lowerPrivilegeChannelMember))
                    .isInstanceOf(PostAccessDeniedException.class);
        }

        @DisplayName("자기 자신은 삭제가 가능하다.")
        @Test
        void shouldAllowDeletionWhenAuthorDeletesOwnPost() {
            Post postCreatedByMemberRole = PostFixture.ChannelA.post_1;
            ChannelMember author = postCreatedByMemberRole.getAuthor();
            postCreatedByMemberRole.validateDeletePermission(author);
        }
    }

    @DisplayName("업데이트 권한 테스트")
    @Nested
    class UpdatePermissionTest {
        @DisplayName("다른 채널 맴버는 업데이트가 불가능하다.")
        @Test
        void shouldDenyUpdateWhenMemberIsFromDifferentChannel() {
            Post postCreateAtChannelA = PostFixture.ChannelA.post_1;
            ChannelMember otherchannelMember = ChannelMemberFixture.ChannelB.OwnerRole.member_1;
            Assertions.assertThatThrownBy(() -> postCreateAtChannelA.validateDeletePermission(otherchannelMember))
                    .isInstanceOf(PostAccessDeniedException.class);
        }

        @DisplayName("자기 자신은 업데이트가 가능하다.")
        @Test
        void shouldAllowUpdateWhenAuthorUpdatesOwnPost() {
            Post post = PostFixture.ChannelA.post_1;
            ChannelMember author = post.getAuthor();
            post.validateUpdateContentPermission(author);
        }

        @DisplayName("다른 사람은 업데이트가 불가능하다.")
        @Test
        void shouldDenyUpdateWhenOtherMemberFromSameChannel() {
            Post post = PostFixture.ChannelA.post_1;
            ChannelMember other = ChannelMemberFixture.ChannelA.MemberRole.member_3;
            Assertions.assertThatThrownBy(() -> post.validateUpdateContentPermission(other))
                    .isInstanceOf(PostAccessDeniedException.class);
        }
    }
}
