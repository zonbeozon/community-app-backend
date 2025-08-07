package com.zonbeozon.post.repository;

import com.zonbeozon.post.dto.PostImageCount;

import java.util.List;

public interface PostImageRepositoryCustom {
    List<PostImageCount> countImagesByPostIds(List<Long> postIds);
}
