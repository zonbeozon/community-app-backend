package com.zonbeozon.post.repository;

import com.zonbeozon.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long>, PostImageRepositoryCustom {
    @Query("select pi FROM PostImage pi JOIN FETCH pi.image WHERE pi.post.id = :postId")
    List<PostImage> findAllByPostIdWithImage(Long postId);

    @Query("select pi FROM PostImage pi JOIN FETCH pi.image WHERE pi.post.id IN :postIds")
    List<PostImage> findAllByPostIdInWithImage(List<Long> postIds);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM PostImage pi WHERE pi.post.id = :postId")
    void deleteAllByPostId(Long postId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM PostImage pi WHERE pi.post.id IN :postId")
    void deleteAllByPostIdIn(List<Long> postId);
}
