package com.zonbeozon.comment.repository;

import com.zonbeozon.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query("SELECT c FROM Comment c JOIN FETCH c.post WHERE c.id = :commentId")
    Optional<Comment> findByIdWithPost(Long commentId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post.id IN :postIds")
    void deleteByPostIdIn(List<Long> postIds);
}
