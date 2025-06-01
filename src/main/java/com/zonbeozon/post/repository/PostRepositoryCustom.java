package com.zonbeozon.post.repository;

import com.zonbeozon.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface PostRepositoryCustom {
    Page<Post> findPagedPost(
            Long channelId,
            String keyword,
            int page,
            int size,
            PostSort sort,
            Sort.Direction direction
    );
}
