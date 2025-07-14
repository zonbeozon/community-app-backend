package com.zonbeozon.global;

import java.util.List;

public interface CursorPage<T> {
    List<T> getContent();
    Long getCursorId();
    Long getTotalElements();
    int getSize();
    boolean isLast();
}
