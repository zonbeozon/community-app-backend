package com.zonbeozon.post.exception;

public class PostException extends RuntimeException {
    public PostException(String message) {
        super(message);
    }

    public PostException(String message, Throwable cause) {
        super(message, cause);
    }
}
