package com.zonbeozon.reaction.repository;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.reaction.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    Optional<CommentReaction> findByCommentAndAuthor(Comment comment, ChannelMember author);
    List<CommentReaction> findByComment(Comment comment);
}
