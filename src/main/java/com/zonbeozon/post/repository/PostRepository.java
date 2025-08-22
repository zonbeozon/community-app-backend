package com.zonbeozon.post.repository;

import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Optional<Post> findTopByChannelOrderByIdDesc(BlogChannel channel);
    List<Post> findByChannelId(Long channelId);

}
