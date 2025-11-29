package com.zonbeozon.post.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.domain.PostMetric;
import com.zonbeozon.post.repository.PostMetricRepository;
import com.zonbeozon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCreateService {
    private final PostRepository postRepository;
    private final ChannelFinder channelFinder;
    private final PostImageService postImageService;
    private final MemberFinder memberFinder;
    private final PostMetricRepository postMetricRepository;

    public Long createPost(
            Long authorId,
            Long channelId,
            String content,
            List<Long> imageIds
    ) {
        Member author = memberFinder.findByIdElseThrow(authorId);
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        PostMetric metric = createPostMetric();
        Post post = Post.create(content, channel, author, metric);
        postRepository.save(post);
        if(imageIds == null || imageIds.isEmpty()) return post.getId();

        postImageService.updatePostImages(post.getId(), imageIds);
        return post.getId();
    }

    private PostMetric createPostMetric() {
        return postMetricRepository.save(new PostMetric());
    }
}
