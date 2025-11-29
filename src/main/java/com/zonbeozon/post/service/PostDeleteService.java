package com.zonbeozon.post.service;

import com.zonbeozon.comment.service.CommentDeleteService;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.reaction.post.service.PostReactionDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostDeleteService {
    private final PostFinder postFinder;
    private final PostRepository postRepository;
    private final PostImageService postImageService;
    private final CommentDeleteService commentDeleteService;
    private final PostReactionDeleteService postReactionDeleteService;

    public void delete(Long postId) {
        Post post = postFinder.findByIdElseThrow(postId);
        postImageService.deletePostImagesByPostId(postId);
        commentDeleteService.deleteByPostId(postId);
        postReactionDeleteService.deleteAll(postId);
        postRepository.deleteById(post.getId());
    }

    public void deleteAllByChannelId(Long channelId) {
        List<Long> postIds = postFinder.findByChannelId(channelId).stream().map(Post::getId).toList();
        postImageService.deletePostImagesByPostIdIn(postIds);
        postReactionDeleteService.deleteAll(postIds);
        commentDeleteService.deleteByPostIdIn(postIds);
        postRepository.deleteAllById(postIds);
    }
}
