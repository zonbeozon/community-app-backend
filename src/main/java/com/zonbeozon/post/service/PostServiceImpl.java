package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.service.ChannelEntityQueryService;
import com.zonbeozon.channel.service.ChannelMemberEntityQueryService;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.exception.PostNotFoundException;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.repository.PostSort;
import com.zonbeozon.post.service.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class PostServiceImpl implements PostEntityQueryService {
    private final PostRepository postRepository;
    private final ChannelEntityQueryService channelEntityQueryService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * todo: 채널 생성 권한 검사 추가
     */
    @Transactional
    public Long addPost(PostAddCommand command, ChannelMember channelMember) {
        Post post = Post.create(command.content(), channelMember);
        postRepository.save(post);
        eventPublisher.publishEvent(new PostCreatedEvent(channelMember.getChannel().getId(), post.getId()));
        return post.getId();
    }

    @Transactional
    public void deletePost(Long postId, ChannelMember channelMember) {
        Post post = getPostByIdOrThrow(postId);
        post.validateDeletePermission(channelMember);
        eventPublisher.publishEvent(new PostDeletedEvent(channelMember.getChannel().getId(), postId));
        postRepository.delete(post);
    }

    @Transactional
    public void updatePostContent(Long postId, ChannelMember channelMember, String content) {
        Post post = getPostByIdOrThrow(postId);
        post.validateUpdateContentPermission(channelMember);
        post.updateContent(content);
        eventPublisher.publishEvent(new PostUpdatedEvent(channelMember.getChannel().getId(), postId));
    }

    @Transactional(readOnly = true)
    public Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId + "는 존재하지 않는 postId 입니다."));
    }

    /**
     * Post 조회
     * 채널 설정에 따라서 채널에 가입해야지만 호출 가능할 수도 있다.
     * @throws com.zonbeozon.channel.exception.ChannelNotFoundException
     * channelId가 존재하지 않을때
     */
    @Transactional(readOnly = true)
    public PagedPostsResponse createPagedPostResponse(
            ChannelMember channelMember,
            Long channelId,
            String searchParam,
            int page,
            int size,
            PostSort sort,
            Sort.Direction direction
    ) {
        //채널Id로 존재하는 채널인지 확인.
        Channel channel = channelEntityQueryService.getChannelByIdOrThrow(channelId);
        channel.validateContentReadPermission(channelMember);
        Page<Post> posts = postRepository.findPagedPost(channelId, searchParam, page, size, sort, direction);
        return PagedPostsResponse.from(posts);
    }

}
