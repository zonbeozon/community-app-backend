package com.zonbeozon.comment.repository;

import com.zonbeozon.comment.entity.Comment;
import com.zonbeozon.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
