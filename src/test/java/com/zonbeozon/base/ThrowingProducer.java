package com.zonbeozon.base;

@FunctionalInterface
public interface ThrowingProducer<T, E extends Exception> {
    T call() throws E;
}