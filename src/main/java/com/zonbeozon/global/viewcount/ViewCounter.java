package com.zonbeozon.global.viewcount;

import java.util.List;

public interface ViewCounter {
    /**
     * @param contentIds 1씩 조회수를 증가시킬 contentId list
     */
    void increase(List<Long> contentIds);
}
