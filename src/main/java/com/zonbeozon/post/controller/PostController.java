package com.zonbeozon.post.controller;

import com.zonbeozon.channel.service.dto.PagedChannelResponse;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.repository.PostSort;
import com.zonbeozon.post.service.PostService;
import com.zonbeozon.post.service.dto.PagedPostsResponse;
import com.zonbeozon.post.service.dto.PostAddCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel/{channelId}/post")
public class PostController {
    private final PostService postService;

    @Operation(
            summary = "POST 생성",
            description = """
                    """
    )
    @PostMapping
    public ResponseEntity<Long> createPost(
            @PathVariable Long channelId,
            @RequestBody
            @Valid
            PostAddRequest request,
            @Parameter(hidden = true) Member member
    ) {
        Long postId = postService.addPost(member, channelId, new PostAddCommand(request.content()));
        return ResponseEntity.ok(postId);
    }

    @Operation(
            summary = "POST 삭제",
            description = """
                    Post작성자, Post작성자보다 권한이 높은 채널 맴버만 삭제 가능하다.
                    """
    )
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long channelId,
            @PathVariable Long postId,
            @Parameter(hidden = true) Member member
    ) {
        postService.deletePost(member, channelId, postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "POST 업데이트",
            description = """
                    Post작성자만 호출 가능하다.
                    """
    )
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long channelId,
            @PathVariable Long postId,
            @RequestBody
            @Valid
            PostUpdateRequest request,
            @Parameter(hidden = true)
            Member member
    ) {
        postService.updatePostContent(member, channelId, postId, request.content());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "POST 검색",
            description = """
                    searchParam이 없다면 모든 POST에 대해 검색.
                    자세한 것은 defaultValue를 참조
                    jwt토큰이 없어도 호출은 가능,
                    하지만 채널 SearchLevel이 Private이라면 채널에 가입된 맴버여야지만 정상적으로 호출된다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PagedChannelResponse.class))),
    })
    @Parameters({
            @Parameter(name = "searchParam", description = "POST 글의 일부", example = "Wen?"),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
            @Parameter(name = "size", description = "한 페이지당 아이템 수", example = "10")
    })
    @GetMapping
    public ResponseEntity<PagedPostsResponse> createPagedPostResponse(
            @PathVariable Long channelId,
            @RequestParam(defaultValue = "") String searchParam,
            @RequestParam(defaultValue = "CREATED_AT") PostSort sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @Parameter(hidden = true)
            Member member

    ) {
        PagedPostsResponse response = postService.createPagedPostResponse(
                member,
                channelId,
                searchParam,
                page,
                size,
                sort,
                direction
        );

        return ResponseEntity.ok(response);
    }
}
