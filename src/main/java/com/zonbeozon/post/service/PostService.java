package com.zonbeozon.post.service;

import com.zonbeozon.auth.exception.AuthException;
import com.zonbeozon.channel.ChannelContext;
import com.zonbeozon.channel.UseChannelContext;
import com.zonbeozon.channel.service.ChannelMemberService;
import com.zonbeozon.channel.service.ChannelService;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.exception.PostNotFoundException;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.service.dto.PostResponse;
import com.zonbeozon.post.service.dto.PostAddCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@UseChannelContext
public class PostService {
    private final PostRepository postRepository;
    private final ChannelService channelService;
    private final ChannelMemberService channelMemberService;

    @Transactional
    public Long addPost(PostAddCommand command, ChannelContext channelContext) {
        Post post = Post.create(command.title(), command.content(), channelContext.getChannelMember());
        return postRepository.save(post).getId();
    }

    @Transactional
    public void deletePost(Long postId, Member member) {
        Post post = getPostByIdOrThrow(postId);
        if(!post.isAuthor(member)) throw new AuthException("post를 삭제할 권한이 없습니다");
        postRepository.delete(post);
    }

    @Transactional
    public void updatePost(Long postId, Member member) {
        Post post = getPostByIdOrThrow(postId);
        if(!post.isAuthor(member)) throw new AuthException("post를 삭제할 권한이 없습니다");
        postRepository.delete(post);
    }


    @Transactional(readOnly = true)
    public Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId + "는 존재하지 않는 postId 입니다."));
    }

    @Transactional(readOnly = true)
    public PostResponse createPostResponse(Long postId) {
        Post post = getPostByIdOrThrow(postId);
        return PostResponse.from(post);
    }

//    @Transactional(readOnly = true)
//    public DetailCryptoDiscussionPostResponse getDetailCryptoDiscussionPostResponse(
//            Long discussionPostId,
//            Pageable commentPageable
//    ) {
//        PagedCryptoDiscussionCommentsResponse commentsResponse = cryptoDiscussionCommentService
//                .getPagedCryptoDiscussionCommentsResponse(discussionPostId, commentPageable);
//        return DetailCryptoDiscussionPostResponse.from(super.getById(discussionPostId), commentsResponse);
//    }
//
//    @Transactional(readOnly = true)
//    public PagedCryptoDiscussionPostsResponse getPagedCryptoDiscussionPostsResponse(
//            final Pageable pageable,
//            final Long currencyId
//    ) {
//        Page<CryptoDiscussionPost> posts = cryptoDiscussionPostRepository.findByCurrencyId(currencyId, pageable);
//        return PagedCryptoDiscussionPostsResponse.fromPagedEntity(posts);
//    }
}
