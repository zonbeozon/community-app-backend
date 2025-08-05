package com.zonbeozon.reaction.repository;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.entity.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByPostAndAuthor(Post post, Member author);
    List<PostReaction> findByPostInAndAuthor(List<Post> posts, Member author);
    List<PostReaction> findByPostIn(List<Post> posts);
}
