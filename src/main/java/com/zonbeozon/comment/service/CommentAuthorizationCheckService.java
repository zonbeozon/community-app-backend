package com.zonbeozon.comment.service;

import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.CheckReturnValue;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentAuthorizationCheckService {
    private final PostAuthorizationCheckService postAuthorizationCheckService;
    private final CommentFinder commentFinder;

    @CheckReturnValue
    public boolean isAuthorOrHasHigherRoleThanAuthor(Long commentId) {
        Comment comment = commentFinder.findByIdWithPostElseThrow(commentId);
        return postAuthorizationCheckService.isAuthorOrHasHigherRoleThanAuthor(comment.getPost().getId());
    }
}
