package com.zonbeozon.post.service;

import com.zonbeozon.auth.exception.AuthException;
import com.zonbeozon.channel.ChannelContext;
import com.zonbeozon.channel.UseChannelContext;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.channel.service.ChannelEntityQueryService;
import com.zonbeozon.channel.service.ChannelMemberEntityQueryService;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.exception.PostNotFoundException;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.repository.PostSort;
import com.zonbeozon.post.service.dto.PagedPostsResponse;
import com.zonbeozon.post.service.dto.PostResponse;
import com.zonbeozon.post.service.dto.PostAddCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@UseChannelContext
public class PostService {
    private final PostRepository postRepository;
    private final ChannelEntityQueryService channelEntityQueryService;
    private final ChannelMemberEntityQueryService channelMemberEntityQueryService;

    @Transactional
    public Long addPost(PostAddCommand command, ChannelMember channelMember) {
        Post post = Post.create(command.content(), channelMember);
        return postRepository.save(post).getId();
    }

    @Transactional
    public void deletePost(Long postId, ChannelMember channelMember) {
        Post post = getPostByIdOrThrow(postId);
        post.validateDeletePermission(channelMember);
        postRepository.delete(post);
    }

    @Transactional
    public void updatePostContent(Long postId, ChannelMember channelMember, String content) {
        Post post = getPostByIdOrThrow(postId);
        post.validateUpdateContentPermission(channelMember);
        post.updateContent(channelMember, content);
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

    /**
     * Post 조회
     * 채널 설정에 따라서 채널에 가입해야지만 호출 가능할 수도 있다.
     * @throws com.zonbeozon.channel.exception.ChannelNotFoundException
     * channelId가 존재하지 않을때
     */
    public PagedPostsResponse createPagedPostResponse(
            Long channelId,
            String searchParam,
            int page,
            int size,
            PostSort sort,
            Sort.Direction direction,
            Member member
    ) {
        //채널Id로 존재하는 채널인지 확인.
        Channel channel = channelEntityQueryService.getChannelByIdOrThrow(channelId);
        //member가 채널에 속하는지 확인
        ChannelMember channelMember = channelMemberEntityQueryService.getByMemberAndChannel(member, channel).orElse(null);
        channel.validateContentReadPermission(channelMember);
        Page<Post> posts = postRepository.findPagedPost(channelId, searchParam, page, size, sort, direction);
        return PagedPostsResponse.from(posts);
    }

}
