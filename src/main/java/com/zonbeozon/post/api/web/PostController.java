package com.zonbeozon.post.api.web;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.global.exception.BadRequestException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.viewcount.CookieViewMarker;
import com.zonbeozon.global.viewcount.ViewCounter;
import com.zonbeozon.post.api.PostCreateApi;
import com.zonbeozon.post.api.PostDeleteApi;
import com.zonbeozon.post.api.PostQueryApi;
import com.zonbeozon.post.api.PostUpdateApi;
import com.zonbeozon.post.dto.*;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Tag(name = "포스트", description = "포스트 관련 엔드포인트")
public class PostController {
    private final CookieViewMarker postViewMarker;
    private final ViewCounter postViewCounter;
    private final PostCreateApi postCreateApi;
    private final PostDeleteApi postDeleteApi;
    private final PostUpdateApi postUpdateApi;
    private final PostQueryApi postQueryApi;

    public PostController(
            @Qualifier("postViewMarker")
            CookieViewMarker postViewMarker,
            @Qualifier("postViewCounter")
            ViewCounter postViewCounter,
            PostCreateApi postCreateApi,
            PostDeleteApi postDeleteApi,
            PostUpdateApi postUpdateApi,
            PostQueryApi postQueryApi
    ) {
        this.postViewMarker = postViewMarker;
        this.postViewCounter = postViewCounter;
        this.postCreateApi = postCreateApi;
        this.postDeleteApi = postDeleteApi;
        this.postUpdateApi = postUpdateApi;
        this.postQueryApi = postQueryApi;
    }

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
    @PostMapping("/channels/{channelId}/posts")
    public ResponseEntity<Long> createPost(
            @PathVariable Long channelId,
            @RequestBody
            @Valid
            PostCreateRequest request
    ) {
        Long postId = postCreateApi.createPost(channelId, request);
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
    @DeleteMapping("posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId
    ) {
        postDeleteApi.deletePost(postId);
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
    @PatchMapping("posts/{postId}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody
            @Valid
            PostUpdateRequest request
    ) {
        postUpdateApi.updatePost(postId, request);
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
                    해당 postId보다 작거나 큰 PostId를 size만큼 반환(cursor)
                    
                    cursorPostId를 가장 최신 post로 설정하고 싶다면 createdAt, postId를 null , inverted를 false로 설정
                    """),
            @Parameter(name = "size", description = "원하는 size, 실제로 응답값은 이보다 작을 수 있다", example = "10"),
            @Parameter(name = "inverted", description = "false라면 해당 cursor 이후의 post를 true라면 이전 post를 반환", example = "true")
    })
    @GetMapping("channels/{channelId}/posts")
    public ResponseEntity<CursorBasedPostsResponse> createCursorBasedPostResponse(
            @PathVariable Long channelId,
            @RequestParam(required = false) LocalDateTime createdAt,
            @RequestParam(required = false) Long postId,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam boolean inverted
    ) {
        if((createdAt == null || postId == null) && inverted)
            throw new BadRequestException(ErrorCode.INVERTED_SEARCH_REQUIRES_CURSOR);
        if(createdAt != null && postId != null) {
            return ResponseEntity.ok(postQueryApi.getCursorBasedPostResponse(channelId, new PostCursor(createdAt, postId), size, inverted));
        }
        if(createdAt == null && postId == null) {
            return ResponseEntity.ok(postQueryApi.getCursorBasedPostResponse(channelId, null, size, false));
        }
        throw new BadRequestException(ErrorCode.INVALID_POST_CURSOR_COMBINATION);
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
            )),
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
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> getPostResponse(
            @PathVariable Long postId
    ) {
        PostResponse response = postQueryApi.getCursorBasedPostResponse(postId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "POST 조회수 증가",
            description = """
                    POST 조회수를 증가시킨다.
                    
                    이미 본 Post id는 쿠키에 기록을 한다
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @PostMapping("/posts/view-count")
    public ResponseEntity<Void> postViewCount(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody
            @Valid
            PostViewCountRequest body
            ) {
        List<Long> notViewedPostIds = body.postIds().stream().filter(id -> !postViewMarker.hasViewed(request, id)).toList();
        postViewCounter.increase(notViewedPostIds);
        postViewMarker.setAsViewed(request, response, notViewedPostIds);
        return ResponseEntity.ok().build();
    }
}
