package com.zonbeozon.fiat.exception;

public class ConversionRateException extends FiatException {
    public ConversionRateException(String message) {
        super(message);
    }

    public ConversionRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
