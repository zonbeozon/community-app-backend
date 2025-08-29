package com.zonbeozon.channel.service;

import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.dto.JoinedBlogChannelInfoListResponse;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.enums.ChannelMemberStatus;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.service.assembler.BlogChannelAssembler;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ChannelResponseTest extends AbstractChannelIntegrationTest {

    @Autowired
    private BlogChannelAssembler channelAssembler;

    private BlogChannel blogChannel_1;
    private BlogChannel blogChannel_2;

    private Member requester;

    @BeforeEach
    void setup() {
        requester = testMemberService.createAndSave();
        testMemberService.setSecurityContext(requester);
        blogChannel_1 = testBlogChannelService.createAndSave("channel_1");
        blogChannel_2 = testBlogChannelService.createAndSave("channel_2");

        //요청자를 blogChannel_1 에 가입시킨다.
        testBlogChannelService.joinAsMember(blogChannel_1, requester);
    }

    @Test
    @DisplayName("주어진 맴버가 포함된 채널만 가져와야 한다.")
    void returnsOnlyChannelsJoinedByMember() {
        JoinedBlogChannelInfoListResponse response = channelAssembler.getJoinedCommunityBlogChannelInfo(requester.getId());
        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).channelInfo().channelId()).isEqualTo(blogChannel_1.getId());
    }

    @Test
    @DisplayName("latest post가 있다면 응답에 포함 시킨다.")
    void sortsChannelsByLatestPostCreatedAtInDescOrder() {
        Post post_1 = testPostService.createAndSave(blogChannel_1, requester);
        blogChannel_1.setLatestPostId(post_1.getId());

        JoinedBlogChannelInfoListResponse response = channelAssembler.getJoinedCommunityBlogChannelInfo(requester.getId());

        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).channelInfo().channelId()).isEqualTo(blogChannel_1.getId());
        assertThat(response.channels().get(0).latestPost().postId()).isEqualTo(post_1.getId());
    }

    @Test
    @DisplayName("상태가 ACTIVE인 채널 맴버만 memberCount에 포함한다.")
    void onlyIncludeActiveMembersInChannelMemberCount() {
        Member member_1 = testMemberService.createAndSave("jun");
        Member member_3 = testMemberService.createAndSave("kim");
        ChannelMember channelMember_1 = testBlogChannelService.joinAsMember(blogChannel_1, member_1);
        ChannelMember channelMember_3 = testBlogChannelService.joinAsMember(blogChannel_1, member_3);
        testBlogChannelService.setChannelMemberStatus(channelMember_1, ChannelMemberStatus.BANNED);
        testBlogChannelService.setChannelMemberStatus(channelMember_3, ChannelMemberStatus.PENDING);

        JoinedBlogChannelInfoListResponse response = channelAssembler.getJoinedCommunityBlogChannelInfo(requester.getId());
        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).channelInfo().memberCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("요청자의 채널에서의 정보가 포함되어야 한다.")
    void includeRequesterMetadataInJoinedChannelResponse() {
        JoinedBlogChannelInfoListResponse response = channelAssembler.getJoinedCommunityBlogChannelInfo(requester.getId());
        assertThat(response.channels()).hasSize(1);
        assertThat(response.channels().get(0).requester().member().memberId()).isEqualTo(requester.getId());
        assertThat(response.channels().get(0).requester().channelRole()).isEqualTo(ChannelRole.CHANNEL_MEMBER);
    }
}
