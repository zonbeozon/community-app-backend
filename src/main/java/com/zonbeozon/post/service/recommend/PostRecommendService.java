package com.zonbeozon.post.service.recommend;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.member.dto.MemberDto;
import com.zonbeozon.member.service.MemberAssembler;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.post.dto.PagedRecommendPostPayload;
import com.zonbeozon.post.dto.RecommendPostDto;
import com.zonbeozon.post.repository.PostRepository;
import com.zonbeozon.post.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostRecommendService {
    private final PostRepository postRepository;
    private final LastScoreUpdatedTimeProvider lastScoreUpdatedTimeProvider;
    private final MemberAssembler memberAssembler;
    private final PostImageService postImageService;

    public PagedRecommendPostPayload recommend(Pageable pageable) {
        Page<Post> posts = postRepository.findPostByContentVisibilityOrderByTotalScoreDesc(pageable, ChannelContentVisibility.PUBLIC);
        postImageService.loadImages(posts.getContent());
        List<Long> authorIds = posts.stream().map(post -> post.getAuthor().getId()).distinct().toList();
        Map<Long, MemberDto> authors = memberAssembler.getMemberResponse(authorIds);
        Page<RecommendPostDto> postResponse = posts.map(post -> RecommendPostDto.from(post, authors.get(post.getAuthor().getId())));
        return PagedRecommendPostPayload.from(postResponse, lastScoreUpdatedTimeProvider.get());
    }
}
