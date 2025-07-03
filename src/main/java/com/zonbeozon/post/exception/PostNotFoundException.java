package com.zonbeozon.post.exception;

public class PostNotFoundException extends PostException {
    public PostNotFoundException() {
        super("Post not found");
    }
}
