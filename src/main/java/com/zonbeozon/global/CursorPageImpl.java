package com.zonbeozon.global;

import java.util.Collections;
import java.util.List;

public class CursorPageImpl<T> implements CursorPage<T> {
    private final List<T> content;
    private final Long cursorId;
    private final Long totalElements;
    private final boolean isLast;
    private final int size;

    public CursorPageImpl(List<T> content, Long cursorId, Long totalElements, boolean isLast, int size) {
        this.content = Collections.unmodifiableList(content);
        this.cursorId = cursorId;
        this.totalElements = totalElements;
        this.isLast = isLast;
        this.size = size;
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public Long getCursorId() {
        return cursorId;
    }

    @Override
    public Long getTotalElements() {
        return totalElements;
    }

    @Override
    public int getSize() {return size;}

    @Override
    public boolean isLast() {
        return isLast;
    }
}
