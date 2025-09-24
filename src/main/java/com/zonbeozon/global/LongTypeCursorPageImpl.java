package com.zonbeozon.global;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class LongTypeCursorPageImpl<T> implements LongTypeCursorPage<T> {
    private final List<T> content;
    private final Long cursor;
    private final Long totalElements;
    private final boolean isInverted;
    private final boolean isLast;
    private final int size;

    public LongTypeCursorPageImpl(List<T> content, Long cursor, Long totalElements, boolean isInverted, boolean isLast, int size) {
        this.content = Collections.unmodifiableList(content);
        this.cursor = cursor;
        this.totalElements = totalElements;
        this.isInverted = isInverted;
        this.isLast = isLast;
        this.size = size;
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public Long getCursor() {
        return cursor;
    }

    @Override
    public Long getTotalElements() {
        return totalElements;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isLast() {
        return isLast;
    }

    @Override
    public boolean isInverted() {
        return isInverted;
    }

    @Override
    public <U> LongTypeCursorPage<U> map(Function<T, U> converter) {
        List<U> convertedContent = this.content.stream()
                .map(converter)
                .toList();

        return new LongTypeCursorPageImpl<>(
                convertedContent,
                this.cursor,
                this.totalElements,
                this.isInverted,
                this.isLast,
                this.size
        );
    }
}
