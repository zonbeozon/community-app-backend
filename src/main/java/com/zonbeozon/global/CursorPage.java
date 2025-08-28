package com.zonbeozon.global;

import java.util.List;
import java.util.function.Function;

public interface CursorPage<T> {
    List<T> getContent();
    Long getCursorId();
    Long getTotalElements();
    int getSize();
    boolean isLast();
    boolean isInverted();
    <U> CursorPage<U> map(Function<T, U> converter);

}
