package com.zonbeozon.post.controller;

import com.zonbeozon.post.dto.PostCreateRequest;

import java.util.List;

public class TestPostCreateRequestBuilder {
    private String content = "test post";
    private List<Long> imageIds;

    public PostCreateRequest build() {
        return new PostCreateRequest(content, imageIds);
    }

    public TestPostCreateRequestBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public TestPostCreateRequestBuilder withImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
        return this;
    }



}
