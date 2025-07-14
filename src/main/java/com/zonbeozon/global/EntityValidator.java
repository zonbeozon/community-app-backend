package com.zonbeozon.global;

import java.util.List;

public interface EntityValidator {
    <T> void validate(T t);
    <T> void validate(List<T> list);
}
