package com.zonbeozon.post.controller;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.post.repository.PostSort;
import com.zonbeozon.post.service.PostService;
import com.zonbeozon.post.service.dto.PagedPostsResponse;
import com.zonbeozon.post.service.dto.PostAddCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel/{channelId}/post")
@Tag(name = "포스트", description = "포스트 관련 엔드포인트")
public class PostController {
    private final PostService postService;

    @Operation(
            summary = "Post 생성",
            description = """
                    post 생성 권한은 채널 마다 다를 수 있다.
                    
                    권한은 channel 엔드포인트로 확인 가능하다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공 - post id 반환",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "integer", format = "int64", description = "채널 ID")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "필드 형식 오류 또는 채널 생성 규칙 위반",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "잘못된 필드 값 전달",
                                            summary = "필수 필드 누락하거나 길이 등 규칙을 위배한 경우",
                                            value = """
                                                {
                                                  "code": "INVALID_ARGUMENT",
                                                  "errors": [
                                                    {
                                                      "field": "content",
                                                      "message": "post content는 {?}자 이상 {?}자 이하여야 합니다."
                                                    }
                                                  ]
                                                }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Post 미지원 채널",
                                            summary = "Post 미지원 채널로 Post 생성 요청을 보냈을때",
                                            value = """
                                                {
                                                  "code": "POST_NOT_SUPPORTED_CHANNEL",
                                                  "message": "post를 작성할 수 없는 채널입니다."
                                                }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "생성 권한이 없을때",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "생성 권한이 없을때",
                                            summary = "채널 별 Post생성 규칙에 따라 권한이 없을때",
                                            value = """
                                                {
                                                  "code": "POST_CREATION_DENIED",
                                                  "message": "post 작성 권한이 없습니다."
                                                }
                                            """
                                    )
                            }
                    )
            )

        }
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
                    Post작성자 혹은 Post작성자보다 권한이 높은 채널 맴버만 삭제 가능하다.
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
                    
                    채널에 가입하지 않아도 호출 가능하지만
                    
                    채널 SearchLevel이 Private이라면 채널에 가입된 맴버여야지만 정상적으로 호출된다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PagedPostsResponse.class),
                    examples = @ExampleObject(
                            name = "게시글 목록 응답 예시",
                            summary = "3개의 게시글과 2명의 작성자가 포함된 응답",
                            value = """
                                {
                                  "members": [
                                    {
                                      "memberId": 1,
                                      "username": "user1",
                                      "profile": "https://example.com/user1.png",
                                      "role": "CHANNEL_OWNER"
                                    },
                                    {
                                      "memberId": 2,
                                      "username": "user2",
                                      "profile": "https://example.com/user2.png",
                                      "role": "CHANNEL_MEMBER"
                                    }
                                  ],
                                  "posts": [
                                    {
                                      "postId": 101,
                                      "content": "첫 번째 게시글 내용",
                                      "authorId": 1,
                                      "createdAt": "2024-07-01T10:00:00",
                                      "updatedAt": "2024-07-01T10:00:00"
                                    },
                                    {
                                      "postId": 102,
                                      "content": "두 번째 게시글 내용",
                                      "authorId": 2,
                                      "createdAt": "2024-07-02T12:00:00",
                                      "updatedAt": "2024-07-02T12:00:00"
                                    },
                                    {
                                      "postId": 103,
                                      "content": "세 번째 게시글 내용",
                                      "authorId": 1,
                                      "createdAt": "2024-07-03T14:00:00",
                                      "updatedAt": "2024-07-03T14:30:00"
                                    }
                                  ],
                                  "size": 3,
                                  "page": 0,
                                  "totalPages": 1,
                                  "totalElements": 3,
                                  "isLastPage": true
                                }
                                """
                    ))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "호출 권한 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "해당 채널의 맴버가 아니고 동시에 채널이 컨텐츠 열람을 막아뒀을때",
                                            value = """
                                                {
                                                  "code": "CONTENT_READ_FORBIDDEN",
                                                  "message": "채널 컨텐츠 접근 권한이 없습니다."
                                                }
                                                """
                                    )
                            }
                    )
            )
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
