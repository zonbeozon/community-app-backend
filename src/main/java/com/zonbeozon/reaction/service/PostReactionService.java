package com.zonbeozon.reaction.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.service.ChannelEntityQueryService;
import com.zonbeozon.channel.service.ChannelMemberEntityQueryService;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostEntityQueryService;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.entity.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostReactionService extends AbstractReactionService<Post, PostReaction> {
    private final PostEntityQueryService postEntityQueryService;
    private final PostReactionRepository postReactionRepository;

    public PostReactionService(
            ChannelMemberEntityQueryService channelMemberEntityQueryService,
            PostEntityQueryService postEntityQueryService,
            PostReactionRepository postReactionRepository,
            ChannelEntityQueryService channelEntityQueryService
    ) {
        super(channelMemberEntityQueryService, channelEntityQueryService);
        this.postEntityQueryService = postEntityQueryService;
        this.postReactionRepository = postReactionRepository;
    }

    @Override
    protected Post getArticleByIdOrThrow(Long articleId) {
        return postEntityQueryService.getPostByIdOrThrow(articleId);
    }

    @Override
    protected Optional<PostReaction> findReactionByArticleAndAuthor(Post article, ChannelMember channelMember) {
        return postReactionRepository.findByPostAndAuthor(article, channelMember);
    }

    @Override
    protected List<PostReaction> findReactionByArticle(Post article) {
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
