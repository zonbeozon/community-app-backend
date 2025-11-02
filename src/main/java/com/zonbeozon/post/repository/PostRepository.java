package com.zonbeozon.post.repository;

import com.zonbeozon.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Optional<Post> findTopByChannelIdOrderByIdDesc(Long channelId);
    List<Post> findByChannelId(Long channelId);

    @Query("SELECT count(p.id) FROM Post p WHERE p.id IN :postIds AND p.channel.id = :channelId")
    long countPostsInChannel(Collection<Long> postIds, Long channelId);

}
