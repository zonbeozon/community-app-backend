package com.zonbeozon.post.repository;

import com.zonbeozon.post.dto.PostImageCount;
import com.zonbeozon.post.entity.PostImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, PostImageRepositoryCustom {
    @EntityGraph(attributePaths = {"image"})
    List<PostImage> findAllByPostId(Long postId);

    @Modifying
    @Query("DELETE FROM PostImage pi WHERE pi.post.id = :postId")
    void deleteAllByPostId(Long postId);
}
