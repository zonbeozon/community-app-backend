package com.zonbeozon.post.service;

import com.zonbeozon.post.entity.Post;

public interface PostEntityQueryService {
    Post getPostByIdOrThrow(Long postId);
}
