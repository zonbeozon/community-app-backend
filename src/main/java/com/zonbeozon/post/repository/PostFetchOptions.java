package com.zonbeozon.post.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostFetchOptions {
    private final boolean isWithAuthor;
    private final boolean isWithImages;
    private final boolean isWithChannel;

    public static class Builder {
        private boolean isWithAuthor = false;
        private boolean isWithImages = false;
        private boolean isWithChannel = false;

        public Builder withAuthor(boolean isWithAuthor) {
            this.isWithAuthor = isWithAuthor;
            return this;
        }

        public Builder withImages(boolean isWithImages) {
            this.isWithImages = isWithImages;
            return this;
        }

        public Builder withChannel(boolean isWithChannel) {
            this.isWithChannel = isWithChannel;
            return this;
        }

        public PostFetchOptions build() {
            return new PostFetchOptions(isWithAuthor, isWithImages, isWithChannel);
        }
    }
}
