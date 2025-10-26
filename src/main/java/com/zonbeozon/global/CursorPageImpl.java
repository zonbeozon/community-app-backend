package com.zonbeozon.global;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class CursorPageImpl<T, C> implements CursorPage<T, C> {
    private final List<T> content;
    private final C cursor;
    private final Long totalElements;
    private final boolean isInverted;
    private final boolean isLast;
    private final int size;

    public CursorPageImpl(List<T> content, C cursor, Long totalElements, boolean isInverted, boolean isLast) {
        this.content = Collections.unmodifiableList(content);
        this.cursor = cursor;
        this.totalElements = totalElements;
        this.isInverted = isInverted;
        this.isLast = isLast;
        this.size = content.size();
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public C getNextCursor() {
        return cursor;
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

    @Override
    public boolean isInverted() {
        return isInverted;
    }

    @Override
    public <U> CursorPage<U, C> map(Function<T, U> converter) {
        List<U> convertedContent = this.content.stream()
                .map(converter)
                .toList();

        return new CursorPageImpl<>(
                convertedContent,
                this.cursor,
                this.totalElements,
                this.isInverted,
                this.isLast
        );
    }
}
