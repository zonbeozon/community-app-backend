package com.zonbeozon.comment.repository;

import com.zonbeozon.post.repository.PostFetchOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentFetchOptions {
    private final boolean isWithAuthor;
    private final boolean isWithPost;

    public static class Builder {
        private boolean isWithAuthor = false;
        private boolean isWithPost = false;

        public CommentFetchOptions.Builder withAuthor(boolean isWithAuthor) {
            this.isWithAuthor = isWithAuthor;
            return this;
        }

        public CommentFetchOptions.Builder withPost(boolean isWithPost) {
            this.isWithPost = isWithPost;
            return this;
        }

        public CommentFetchOptions build() {
            return new CommentFetchOptions(isWithAuthor, isWithPost);
        }
    }
}
