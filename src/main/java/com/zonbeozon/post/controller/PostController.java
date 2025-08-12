package com.zonbeozon.post.controller;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.post.dto.*;
import com.zonbeozon.post.service.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "포스트", description = "포스트 관련 엔드포인트")
public class PostController {
    private final PostCreator postCreator;
    private final PostUpdater postUpdater;
    private final PostRemover postRemover;
    private final SecuredPostAssembler securedPostAssembler;

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
                    responseCode = "201",
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
                                                      "message": "post content는 ?자 이상 ?자 이하여야 합니다."
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
                                                  "code": "NOT_INFO_CHANNEL",
                                                  "message": "INFO채널이 아닙니다."
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
                                            summary = "Post생성 규칙에 따라 권한이 없을때",
                                            value = """
                                                {
                                                  "code": "ACCESS_DENIED",
                                                  "message": "접근 권한이 없습니다."
                                                }
                                            """
                                    )
                            }
                    )
            )
        }
    )
    @PostMapping("/channel/{channelId}/post")
    public ResponseEntity<Long> createPost(
            @PathVariable Long channelId,
            @RequestBody
            @Valid
            PostCreateRequest request
    ) {
        Long postId = postCreator.addPost(channelId, new PostCreateCommand(request.content(), request.imageIds()));
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @Operation(
            summary = "POST 삭제",
            description = """
                    Post작성자 혹은 Post작성자보다 권한이 높은 채널 맴버만 삭제 가능하다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "성공 - 응답 바디 없음"
            )
    })
    @DeleteMapping("post/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId
    ) {
        postRemover.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "POST 업데이트",
            description = """
                    Post작성자만 호출 가능하다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공 - 응답 바디 없음"
            )
    })
    @PatchMapping("post/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody
            @Valid
            PostUpdateRequest request
    ) {
        postUpdater.updateContent(postId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "커서 기반 과거 POST 검색",
            description = """
                    채널에 가입해야지만 호출 가능
                    
                    cursor기반으로 postId를 기준으로 DESC순으로 리턴된다(postId는 생성순으로 커지기 때문에 Id가 줄어들 수록 과거에 만들어진 Post이다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CursorBasedPostsResponse.class)
            )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "호출 권한 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "해당 채널의 맴버가 아닐때",
                                            value = """
                                                {
                                                  "code": "ACCESS_DENIED",
                                                  "message": "접근 권한이 없습니다."
                                                }
                                                """
                                    )
                            }
                    )
            )
    })
    @Parameters({
            @Parameter(name = "cursorPostId", description = """
                    해당 postId보다 작은 PostId를 size만큼 반환(cursor)
                    
                    cursorPostId를 가장 최신 post로 설정하고 싶다면 null로 설정
                    """),
            @Parameter(name = "size", description = "원하는 size, 실제로 응답값은 이보다 작을 수 있다", example = "10")
    })
    @GetMapping("channel/{channelId}/post")
    public ResponseEntity<CursorBasedPostsResponse> createCursorBasedPostResponse(
            @PathVariable Long channelId,
            @RequestParam(required = false) Long cursorPostId,
            @RequestParam(defaultValue = "20") int size
    ) {
        CursorBasedPostsResponse response = securedPostAssembler.createCursorBasedPostResponse(
                channelId,
                cursorPostId,
                size
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "단일 POST 검색",
            description = """
                    채널에 가입해야지만 호출 가능
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PostResponse.class)
            )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "호출 권한 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "해당 채널의 맴버가 아닐때",
                                            value = """
                                                {
                                                  "code": "ACCESS_DENIED",
                                                  "message": "접근 권한이 없습니다."
                                                }
                                                """
                                    )
                            }
                    )
            )
    })
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> getPostResponse(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(securedPostAssembler.createPostResponse(postId));
    }
}
