package com.zonbeozon.channel.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.dto.JoinedBlogChannelListResponse;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostBuilder;
import com.zonbeozon.post.entity.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ChannelResponseTest {

    @Autowired
    private BlogChannelAssembler channelAssembler;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ChannelRemover channelRemover;

    private BlogChannel blogChannel_1;
    private BlogChannel blogChannel_2;

    private Member requester;

    @Autowired
    private ChannelRepository channelRepository;

    @BeforeEach
    void setup() {
        requester = new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
        blogChannel_1 = (BlogChannel) new TestChannelBuilder().withTitle("blog_channel_1").withType(ChannelType.BLOG).persist(entityManager);
        blogChannel_2 = (BlogChannel) new TestChannelBuilder().withTitle("blog_channel_2").withType(ChannelType.BLOG).persist(entityManager);
    }

    @Test
    @DisplayName("자신이 속한 채널만 가져와야 한다.")
    void returnsOnlyChannelsJoinedByMember() {
        new TestChannelMemberBuilder(requester, blogChannel_1).persist(entityManager);
        JoinedBlogChannelListResponse response = channelAssembler.createJoinedCommunityBlogChannelResponse();
        assertThat(response.channels()).hasSize(1);

        assertThat(response.channels().get(0).channelId()).isEqualTo(blogChannel_1.getId());
    }


    @Test
    @DisplayName("최근 Post 작성일 기준 Desc Order로 가져와야 한다.")
    void sortsChannelsByLatestPostCreatedAtInDescOrder() {
        new TestChannelMemberBuilder(requester, blogChannel_1).persist(entityManager);
        new TestChannelMemberBuilder(requester, blogChannel_2).persist(entityManager);
        new TestPostBuilder(blogChannel_1, requester).persist(entityManager);
        Post post_1 = new TestPostBuilder(blogChannel_1, requester).persist(entityManager);
        blogChannel_1.setLatestPostId(post_1.getId());
        new TestPostBuilder(blogChannel_2, requester).persist(entityManager);
        Post post_2 = new TestPostBuilder(blogChannel_2, requester).persist(entityManager);
        blogChannel_2.setLatestPostId(post_2.getId());

        JoinedBlogChannelListResponse response = channelAssembler.createJoinedCommunityBlogChannelResponse();


        assertThat(response.channels()).hasSize(2);
        assertThat(response.channels().get(0).channelId()).isEqualTo(blogChannel_2.getId());
        assertThat(response.channels().get(0).latestPost().postId()).isEqualTo(post_2.getId());
        assertThat(response.channels().get(1).channelId()).isEqualTo(blogChannel_1.getId());
        assertThat(response.channels().get(1).latestPost().postId()).isEqualTo(post_1.getId());
    }

    @Test
    @DisplayName("삭제된 채널은 가져오면 안된다")
    void shouldNotRetrieveSoftDeletedChannels() {
        new TestChannelMemberBuilder(requester, blogChannel_1).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);
        channelRemover.removeChannel(blogChannel_1.getId());
        JoinedBlogChannelListResponse response = channelAssembler.createJoinedCommunityBlogChannelResponse();
        assertThat(response.totalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("상태가 ACTIVE인 채널 맴버만 memberCount에 포함한다.")
    void onlyIncludeActiveMembersInChannelMemberCount() {
        Member member_1 = new TestMemberBuilder("jun", "jun@gmail.com").persist(entityManager);
        Member member_2 = new TestMemberBuilder("yunghi", "yunghi@gmail.com").persist(entityManager);
        Member member_3 = new TestMemberBuilder("kim", "kim@gmail.com").persist(entityManager);
        new TestChannelMemberBuilder(requester, blogChannel_1).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);
        new TestChannelMemberBuilder(member_1, blogChannel_1).withStatus(ChannelMemberStatus.ACTIVE).withRole(ChannelRole.CHANNEL_MEMBER).persist(entityManager);
        new TestChannelMemberBuilder(member_2, blogChannel_1).withStatus(ChannelMemberStatus.KICKED).withRole(ChannelRole.CHANNEL_MEMBER).persist(entityManager);
        new TestChannelMemberBuilder(member_3, blogChannel_1).withStatus(ChannelMemberStatus.PENDING).withRole(ChannelRole.CHANNEL_MEMBER).persist(entityManager);

        JoinedBlogChannelListResponse response = channelAssembler.createJoinedCommunityBlogChannelResponse();
        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).memberCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("요청자의 채널에서의 상태가 포함되어야 한다.")
    void includeRequesterMetadataInJoinedChannelResponse() {
        Member member_1 = new TestMemberBuilder("jun", "jun@gmail.com").persist(entityManager);
        new TestChannelMemberBuilder(requester, blogChannel_1).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);
        new TestChannelMemberBuilder(member_1, blogChannel_1).withStatus(ChannelMemberStatus.ACTIVE).withRole(ChannelRole.CHANNEL_MEMBER).persist(entityManager);

        JoinedBlogChannelListResponse response = channelAssembler.createJoinedCommunityBlogChannelResponse();
        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).requester().memberId()).isEqualTo(requester.getId());
    }
}
