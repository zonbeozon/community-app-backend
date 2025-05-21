package com.zonbeozon.common.fetch;

public class FetchException extends RuntimeException {
    public FetchException(String message) {
        super(message);
    }
    public FetchException(Throwable cause) {
        super(cause);
    }
}
