package com.zonbeozon.post.repository;

import com.zonbeozon.post.entity.PostImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @EntityGraph(attributePaths = {"image"})
    List<PostImage> findAllByPostId(Long postId);
}
