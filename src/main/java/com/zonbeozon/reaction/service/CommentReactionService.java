package com.zonbeozon.reaction.service;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.service.ChannelMemberService;
import com.zonbeozon.channel.service.ChannelService;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.service.CommentService;
import com.zonbeozon.reaction.entity.CommentReaction;
import com.zonbeozon.reaction.entity.ReactionType;
import com.zonbeozon.reaction.repository.CommentReactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentReactionService extends AbstractReactionService<Comment, CommentReaction> {
    private final CommentService commentService;
    private final CommentReactionRepository commentReactionRepository;

    public CommentReactionService(
            ChannelMemberService channelMemberService,
            CommentService commentService,
            CommentReactionRepository commentReactionRepository,
            ChannelService channelService
    ) {
        super(channelMemberService, channelService);
        this.commentService = commentService;
        this.commentReactionRepository = commentReactionRepository;
    }

    @Override
    protected Comment getArticleByIdOrThrow(Long articleId) {
        return commentService.getByIdOrThrow(articleId);
    }

    @Override
    protected Optional<CommentReaction> findReactionByArticleAndAuthor(Comment article, ChannelMember channelMember) {
        return commentReactionRepository.findByCommentAndAuthor(article, channelMember);
    }

    @Override
    protected List<CommentReaction> findReactionByArticle(Comment article) {
        return commentReactionRepository.findByComment(article);
    }

    @Override
    protected void save(CommentReaction reaction) {
        commentReactionRepository.save(reaction);
    }

    @Override
    protected void delete(CommentReaction reaction) {
        commentReactionRepository.delete(reaction);
    }

    @Override
    protected CommentReaction createReaction(Comment article, ReactionType reactionType, ChannelMember channelMember) {
        return CommentReaction.create(article, reactionType, channelMember);
    }
}
