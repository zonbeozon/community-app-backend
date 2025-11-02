package com.zonbeozon.reaction.post.repository;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.domain.Post;
import com.zonbeozon.reaction.post.entity.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long>, CustomPostReactionRepository {
    Optional<PostReaction> findByPostAndAuthor(Post post, Member author);
    List<PostReaction> findByPostInAndAuthor(List<Post> posts, Member author);

    @Modifying
    @Query("DELETE FROM PostReaction pr WHERE pr.post.id IN :postIds")
    void deleteByPostIdIn(List<Long> postIds);
}
