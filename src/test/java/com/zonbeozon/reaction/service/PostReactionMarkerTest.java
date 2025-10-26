package com.zonbeozon.reaction.service;

import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.base.TestChannelService;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.post.entity.PostReaction;
import com.zonbeozon.reaction.post.entity.ReactionType;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import com.zonbeozon.reaction.post.service.PostReactionMarker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PostReactionMarkerTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostReactionMarker reactionMarker;
    @Autowired
    private PostReactionRepository postReactionRepository;
    @Qualifier("testChannelService")
    @Autowired
    private TestChannelService testChannelService;

    @DisplayName("좋아요 마크시 저장되어야 한다.")
    @Test
    void testMarkingLikeSavesReaction() {
        Member member = testMemberService.createAndSave();
        BlogChannel channel = testBlogChannelService.createAndSave();
        testChannelService.joinAsMember(channel,member);
        Post post = testPostService.createAndSave(channel, member);
        reactionMarker.mark(member.getId(), post.getId(), ReactionType.LIKE);
        PostReaction reaction = postReactionRepository.findByPostAndAuthor(post, member).get();
        Assertions.assertThat(reaction.getReactionType()).isEqualTo(ReactionType.LIKE);
    }
}
