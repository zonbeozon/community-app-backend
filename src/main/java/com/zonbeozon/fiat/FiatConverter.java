package com.zonbeozon.fiat;

public interface FiatConverter {
    void convert(Object source);
    boolean isSupported(Object source);
}
