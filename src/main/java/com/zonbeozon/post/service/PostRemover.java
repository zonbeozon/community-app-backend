package com.zonbeozon.post.service;

import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.post.dto.PostDeletedEvent;
import com.zonbeozon.post.repository.PostFetchOptions;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.post.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostRemover {
    private final PostFinder postFinder;
    private final ApplicationEventPublisher eventPublisher;
    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final PostReactionRepository postReactionRepository;
    private final CommentRepository commentRepository;

    public void deletePost(Long postId) {
        Post post = postFinder.findByIdElseThrow(postId, new PostFetchOptions.Builder().withImages(true).build());

        //postImage 삭제
        postImageService.deletePostImagesByPostId(postId);

        //comment, postReaction은 cascade option을 통해 삭제한다.
        postRepository.deleteById(post.getId());

        eventPublisher.publishEvent(new PostDeletedEvent(post.getChannel().getId(), postId));
    }

    public void deleteAllPostsByChannelId(Long channelId) {
        List<Long> postIds = postFinder.findByChannelId(channelId).stream().map(Post::getId).toList();
        postImageService.deletePostImagesByPostIdIn(postIds);
        postReactionRepository.deleteByPostIdIn(postIds);
        commentRepository.deleteByPostIdIn(postIds);
        postRepository.deleteAllById(postIds);
    }
}
