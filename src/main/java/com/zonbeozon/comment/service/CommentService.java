package com.zonbeozon.comment.service;

import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.exception.CommentNotFoundException;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;
    private final CommentRepository commentRepository;

//    @Transactional
//    public Long add(DiscussionCommentAddRequest request, Member member) {
//        DiscussionComment discussionComment =
//    }
//
//    @Transactional(readOnly = true)
//    public PagedDiscussionCommentsResponse getPagedCryptoDiscussionCommentsResponse(
//            final Long discussionPostId,
//            final Pageable pageable
//    ) {
//        Page<CryptoDiscussionComment> comments = cryptoDiscussionCommentRepository
//                .findByCryptoDiscussionPostId(discussionPostId, pageable);
//        return PagedDiscussionCommentsResponse.fromPagedEntity(comments);
//    }
//    @Transactional
//    public Long add(
//            final T t,
//            final DiscussionCommentAddRequest request
//    ) {
//        Member member = loginService.getCurrentMember();
//        validator.validateContent(request.content());
//        t.initAll(request.content(), member);
//        return repository.save(t).getId();
//    }
//
//    @Transactional
//    public void delete(final Long discussionCommentId) {
//        validateAuthorOrAdmin(discussionCommentId);
//        repository.delete(getById(discussionCommentId));
//    }
//
//    @Transactional
//    public void update(
//            final Long discussionCommentId,
//            final DiscussionCommentUpdateRequest request
//    ) {
//        validateAuthorOrAdmin(discussionCommentId);
//
//        validator.validateContent(request.content());
//
//        T comment = getById(discussionCommentId);
//        comment.updateContent(request.content());
//        repository.save(comment);
//    }
//
    @Transactional(readOnly = true)
    public Comment getByIdOrThrow(Long commentId) {
         return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId + "은 존재하지 않는 commentId 입니다."));
    }
//
//    private boolean isAuthor(final Long discussionCommentId, final Long memberId) {
//        return getById(discussionCommentId).getAuthor().getId().equals(memberId);
//    }
//
//    private void validateAuthorOrAdmin(final Long discussionCommentId) {
//        Member member = loginService.getCurrentMember();
//        if (!isAuthorOrAdmin(discussionCommentId, member))
//            throw new AccessDeniedException("어드민이나 글쓴이만 해당 엔드포인트를 호출할 수 있습니다.");
//    }
//
//    private boolean isAuthorOrAdmin(final Long discussionCommentId, final Member member) {
//        return isAuthor(discussionCommentId, member.getId()) || member.isAdmin();
//    }
}
