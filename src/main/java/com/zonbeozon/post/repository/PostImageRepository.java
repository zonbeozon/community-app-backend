package com.zonbeozon.post.repository;

import com.zonbeozon.post.dto.PostImageCount;
import com.zonbeozon.post.entity.PostImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @EntityGraph(attributePaths = {"image"})
    List<PostImage> findAllByPostId(Long postId);

    @EntityGraph(attributePaths = {"image"})
    List<PostImage> findAllByPostIdIn(List<Long> postId);

    @Query(
            """
            SELECT new com.zonbeozon.post.dto.PostImageCount(pi.post.id, COUNT(pi.id))
            FROM PostImage pi
            WHERE pi.post.id IN :postIds
            GROUP BY pi.post.id
            """
    )
    List<PostImageCount> countImagesByPostIds(List<Long> postIds);
}
