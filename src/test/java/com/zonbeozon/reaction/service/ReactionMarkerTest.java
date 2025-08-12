package com.zonbeozon.reaction.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.TestPostBuilder;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.enums.ReactionContentType;
import com.zonbeozon.reaction.enums.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ReactionMarkerTest {
    @Autowired
    private ReactionMarker reactionMarker;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PostReactionRepository postReactionRepository;


    @DisplayName("좋아요 마크시 저장되어야 한다.")
    @Test
    void testMarkingLikeSavesReaction() {
        Member member = new TestMemberBuilder().persistAndSetSecurityContext(entityManager);
        BlogChannel blogChannel = (BlogChannel) new TestChannelBuilder().persist(entityManager);
        new TestChannelMemberBuilder(member, blogChannel).persist(entityManager);
        Post post = new TestPostBuilder(blogChannel, member).persist(entityManager);
        reactionMarker.mark(post.getId(), ReactionContentType.POST, ReactionType.LIKE);
        PostReaction reaction = postReactionRepository.findByPostAndAuthor(post, member).get();
        Assertions.assertThat(reaction.getReactionType()).isEqualTo(ReactionType.LIKE);
    }
}
