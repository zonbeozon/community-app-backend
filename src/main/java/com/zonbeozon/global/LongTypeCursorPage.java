package com.zonbeozon.global;

import java.util.function.Function;

public interface LongTypeCursorPage<T> extends CursorPage<T, Long> {
    <U> LongTypeCursorPage<U> map(Function<T, U> converter);
}
