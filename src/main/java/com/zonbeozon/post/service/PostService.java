package com.zonbeozon.post.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.repository.PostSort;
import com.zonbeozon.post.service.dto.PagedPostsResponse;
import com.zonbeozon.post.service.dto.PostAddCommand;
import org.springframework.data.domain.Sort;

public interface PostService {
    Long addPost(Member member, Long channelId, PostAddCommand command);
    void deletePost(Member member, Long channelId, Long postId);
    void updatePostContent(Member member, Long channelId, Long postId, String content);
    PagedPostsResponse createPagedPostResponse(
            Member member,
            Long channelId,
            String searchParam,
            int page,
            int size,
            PostSort sort,
            Sort.Direction direction
    );
}
