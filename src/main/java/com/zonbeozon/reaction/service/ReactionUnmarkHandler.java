package com.zonbeozon.reaction.service;

import com.zonbeozon.reaction.enums.ReactionContentType;

public interface ReactionUnmarkHandler {
    void unmark(Long requesterId, Long contentId);
    boolean isSupport(ReactionContentType reactionContentType);
}
