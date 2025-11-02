package com.zonbeozon.base;

import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.comment.repository.CommentRepository;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestCommentService {
    @Autowired
    private CommentRepository commentRepository;

    private static final String DEFAULT_CONTENT = "default comment";

    public Comment createAndSave(Member author, Post post) {
        Comment comment = new Comment(DEFAULT_CONTENT, author, post);
        return commentRepository.save(comment);
    }
}
