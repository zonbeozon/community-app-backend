package com.zonbeozon.post.service.metric.viewcount;

import java.util.List;

public interface PostViewCounter {
    /**
     * @param postIds 1씩 조회수를 증가시킬 postId list
     */
    void increase(List<Long> postIds);
}
