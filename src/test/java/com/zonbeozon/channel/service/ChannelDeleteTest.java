package com.zonbeozon.channel.service;

import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.dto.ChannelDeletedEvent;
import com.zonbeozon.channel.entity.*;

import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.repository.BannedChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelProfileRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.image.TestMockImageBuilder;
import com.zonbeozon.image.entity.Image;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChannelDeleteTest extends AbstractChannelIntegrationTest {
    @Autowired
    private ChannelRemover channelRemover;
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    @Autowired
    private EntityManager entityManager;

    private Member member;
    private Channel channel;
    private ChannelMember channelMember;

    @BeforeEach
    void createChannelAndJoinAsMember() {
        //맴버 채널 생성 후 Member로 가입
        member = testMemberService.createAndSave();
        channel = testBlogChannelService.createAndSave();
        channelMember = testBlogChannelService.joinAsMember(channel, member);
    }

    @Test
    @DisplayName("채널 삭제 이벤트를 발생시킨다.")
    void publishChannelDeletedEventOnChannelRemoval() {
        channelRemover.removeChannel(channel.getId());
        List<ChannelDeletedEvent> events = applicationEvents.stream(ChannelDeletedEvent.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).channelId()).isEqualTo(channel.getId());
    }

    @Test
    @DisplayName("채널 삭제시 채널이 삭제 되어야한다.")
    void ChangeStatusToDeletedWhenChannelIsRemoved() {
        channelRemover.removeChannel(channel.getId());
        Assertions.assertThat(channelRepository.findById(channel.getId())).isEmpty();
    }

    @Nested
    @DisplayName("연관된_엔터티_삭제_테스트")
    class DeletingRelatedEntitiesTest {
        @Autowired
        private PostRepository postRepository;
        @Autowired
        private ChannelProfileRepository channelProfileRepository;
        @Autowired
        private ChannelMemberBanService channelMemberBanService;
        @Autowired
        private BannedChannelMemberRepository bannedChannelMemberRepository;

        @Test
        @DisplayName("만일 BlogChannel이라면 채널 내 포스트가 삭제되어야 한다.")
        void DeletePostsInChannel() {
            assert channel instanceof BlogChannel;
            Post post = testPostService.createAndSave((BlogChannel) channel, member);
            channelRemover.removeChannel(channel.getId());

            Assertions.assertThat(channelRepository.findById(channel.getId())).isEmpty();
            Assertions.assertThat(postRepository.findById(post.getId())).isEmpty();
        }

        @Test
        @DisplayName("채널 프로필이 존재한다면 삭제 되어야 한다.")
        void DeleteChannelProfileIfExists() {
            testBlogChannelService.changeRole(channelMember, ChannelRole.CHANNEL_OWNER);
            Image image = new TestMockImageBuilder(member,"dummy").persist(entityManager);
            testBlogChannelService.setChannelProfile(channel, image);
            ChannelProfile profile = channel.getProfile();
            channelRemover.removeChannel(channel.getId());
            Assertions.assertThat(channelProfileRepository.findById(profile.getId())).isEmpty();
        }

        @Test
        @DisplayName("삭제될때 벤된 유저도 삭제되어야 한다.")
        void removeChannelAlsoDeletesBannedUsers() {
            channelMemberBanService.ban(channel.getId(), member.getId(), null);
            channelRemover.removeChannel(channel.getId());
            Assertions.assertThat(bannedChannelMemberRepository.findByChannelIdAndMemberId(channel.getId(), member.getId())).isEmpty();
        }
    }
}
