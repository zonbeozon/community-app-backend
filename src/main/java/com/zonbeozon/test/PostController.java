package com.zonbeozon.test;

import com.zonbeozon.channel.ChannelContext;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.service.PostService;
import com.zonbeozon.post.service.dto.PostAddCommand;
import com.zonbeozon.post.service.dto.PostResponse;
import com.zonbeozon.post.controller.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final SimpMessagingTemplate messagingTemplate;
    private final PostService postService;

    @PostMapping("/channel/{channelId}/post")
    public ResponseEntity<Void> createPost(
            @PathVariable("channelId") Long channelId,
            PostAddRequest request,
            Member member
    ) {
        Long postId = postService.addPost(
                new PostAddCommand(request.title(), request.message()),
                ChannelContext.with(channelId, member)
        );
        PostResponse postResponse = postService.createPostResponse(postId);
        messagingTemplate.convertAndSend("/`topic`/channel/" + channelId + "/post", postResponse);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/channel/{channelId}/post/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Member member
    ) {
        postService.deletePost(postId, member);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/channel/{channelId}/post/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long channelId,
            @PathVariable Long postId,
            PostUpdateRequest request,
            Member member
    ) {
        return ResponseEntity.noContent().build();
    }
}
