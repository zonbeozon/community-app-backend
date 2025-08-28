package com.zonbeozon.global.viewcount;

import com.zonbeozon.global.entity.ContentEntity;

import java.util.Collection;
import java.util.List;

public interface ContentEntityFinder {
    List<? extends ContentEntity> findByIdIn(Collection<Long> ids);
}
