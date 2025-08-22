package com.zonbeozon.base;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.enums.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
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
