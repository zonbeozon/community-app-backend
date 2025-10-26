package com.zonbeozon.reaction.post.service;

import com.zonbeozon.reaction.post.entity.ReactionType;

public interface ReactionMarkHandler {
    void mark(Long requesterId, Long contentId, ReactionType reactionType);
}
