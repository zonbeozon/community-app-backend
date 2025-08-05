package com.zonbeozon.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PostImageCount {
    private Long postId;
    private Long count;
}
