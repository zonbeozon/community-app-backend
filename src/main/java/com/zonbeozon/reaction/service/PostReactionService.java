package com.zonbeozon.reaction.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.service.ChannelMemberService;
import com.zonbeozon.channel.service.ChannelService;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostService;
import com.zonbeozon.reaction.entity.PostReaction;
import com.zonbeozon.reaction.entity.ReactionType;
import com.zonbeozon.reaction.repository.PostReactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostReactionService extends AbstractReactionService<Post, PostReaction> {
    private final PostService postService;
    private final PostReactionRepository postReactionRepository;

    public PostReactionService(
            ChannelMemberService channelMemberService,
            PostService postService,
            PostReactionRepository postReactionRepository,
            ChannelService channelService
    ) {
        super(channelMemberService, channelService);
        this.postService = postService;
        this.postReactionRepository = postReactionRepository;
    }

    @Override
    protected Post getArticleByIdOrThrow(Long articleId) {
        return postService.getPostByIdOrThrow(articleId);
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
