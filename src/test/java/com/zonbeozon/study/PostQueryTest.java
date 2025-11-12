package com.zonbeozon.study;

import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TestTransaction;

public class PostQueryTest extends AbstractChannelIntegrationTest {
    @Autowired
    private PostRepository postRepository;
    @Test
    @DisplayName("Post.getChannel().getId()로 추가 쿼리발생여부 체크")
    void dd() {
        Member member = testMemberService.createAndSave();
        BlogChannel blogChannel = testBlogChannelService.createAndSave();
        testBlogChannelService.joinAsMember(blogChannel, member);
        Post post = testPostService.createAndSave(blogChannel, member);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        assertThatDb(() -> postRepository.findById(post.getId()).get().getChannel().getId()).hasBeenCalledTimes(1);
    }
}
