package com.zonbeozon.base;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.reaction.post.entity.PostReaction;
import com.zonbeozon.reaction.post.entity.ReactionType;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestPostReactionService {
    @Autowired
    private PostReactionRepository postReactionRepository;
    public PostReaction createAndSave(Post post, ReactionType reactionType, Member author) {
        PostReaction postReaction = PostReaction.create(post, reactionType, author);
        return postReactionRepository.save(postReaction);
    }
}
