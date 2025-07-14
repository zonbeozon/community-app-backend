package com.zonbeozon.reaction.service;

import com.zonbeozon.reaction.enums.ReactionContentType;
import com.zonbeozon.reaction.enums.ReactionType;

public interface ReactionMarkHandler {
    void mark(Long contentId, ReactionType reactionType);
    boolean isSupport(ReactionContentType reactionContentType);
}
