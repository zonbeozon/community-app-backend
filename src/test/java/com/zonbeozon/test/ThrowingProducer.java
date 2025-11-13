package com.zonbeozon.test;

@FunctionalInterface
public interface ThrowingProducer<T, E extends Exception> {
    T call() throws E;
}