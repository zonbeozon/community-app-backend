package com.zonbeozon.comment.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.security.ChannelAction;
import com.zonbeozon.channel.security.CheckChannelAccess;
import com.zonbeozon.comment.dto.CommentCreatedEvent;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.service.PostFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCreator {
    private final PostFinder postFinder;
    private final AuthenticationService authenticationService;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @CheckChannelAccess(ChannelAction.COMMENT_CREATE)
    public Long addComment(Long postId, String content) {
        Post post = postFinder.findById(postId);
        Member member = authenticationService.getCurrentMember();
        Comment comment = new Comment(content, member, post);
        commentRepository.save(comment);
        eventPublisher.publishEvent(new CommentCreatedEvent(postId, comment.getId()));
        return comment.getId();
    }
}
