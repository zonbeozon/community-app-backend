package com.zonbeozon.comment.service;

import com.zonbeozon.comment.dto.CommentCreatedEvent;
import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.repository.PostFetchOptions;
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
    private final MemberFinder memberFinder;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long addComment(Long memberId, Long postId, String content) {
        Post post = postFinder.findByIdElseThrow(postId, new PostFetchOptions.Builder().withChannel(true).build());
        Member member = memberFinder.findByIdElseThrow(memberId);
        Comment comment = new Comment(content, member, post);
        commentRepository.save(comment);
        eventPublisher.publishEvent(new CommentCreatedEvent(post.getChannel().getId(), postId, comment.getId()));
        return comment.getId();
    }
}
