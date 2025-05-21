package com.zonbeozon.reaction.repository;

import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.post.entity.Post;
import com.zonbeozon.reaction.entity.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByPostAndAuthor(Post post, ChannelMember author);
    List<PostReaction> findByPost(Post post);
}
