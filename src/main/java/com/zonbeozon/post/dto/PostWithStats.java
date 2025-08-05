package com.zonbeozon.post.dto;

import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.dto.ReactionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostWithStats {
    private Post post;
    private Long commentCount;
    private ReactionResponse reactionResponse;
}
