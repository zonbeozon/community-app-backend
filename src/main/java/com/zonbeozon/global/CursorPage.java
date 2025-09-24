package com.zonbeozon.global;

import java.util.List;
import java.util.function.Function;

public interface CursorPage<T, C> {
    List<T> getContent();
    C getCursor();
    Long getTotalElements();
    int getSize();
    boolean isLast();
    boolean isInverted();
    <U> CursorPage<U, C> map(Function<T, U> converter);
}
