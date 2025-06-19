package com.zonbeozon.reaction.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostEntityQueryService;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.entity.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostReactionService extends AbstractReactionService<Post, PostReaction> {
    private final PostEntityQueryService postEntityQueryService;
    private final PostReactionRepository postReactionRepository;

    @Override
    protected Post getArticleByIdOrThrow(Long articleId) {
        return postEntityQueryService.getPostByIdOrThrow(articleId);
    }

    @Override
    protected Optional<PostReaction> getReactionByArticleAndAuthor(Post article, ChannelMember channelMember) {
        return postReactionRepository.findByPostAndAuthor(article, channelMember);
    }

    @Override
    protected List<PostReaction> getReactionByArticle(Post article) {
        return postReactionRepository.findByPost(article);
    }

    @Override
    protected void save(PostReaction reaction) {
        postReactionRepository.save(reaction);
    }

    @Override
    protected void delete(PostReaction reaction) {
        postReactionRepository.delete(reaction);
    }

    @Override
    protected PostReaction createReaction(Post article, ReactionType reactionType, ChannelMember channelMember) {
        return PostReaction.create(article, reactionType, channelMember);
    }
}
