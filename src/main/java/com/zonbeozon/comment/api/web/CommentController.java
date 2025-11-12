package com.zonbeozon.comment.api.web;

import com.zonbeozon.comment.api.CommentCommandApi;
import com.zonbeozon.comment.api.CommentQueryApi;
import com.zonbeozon.comment.dto.CommentAddRequest;
import com.zonbeozon.comment.dto.CommentsWithAuthorResponse;
import com.zonbeozon.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "댓글", description = "댓글 관련 엔드포인트")
public class CommentController {
    private final CommentQueryApi commentQueryApi;
    private final CommentCommandApi commentCommandApi;

    @Operation(
            summary = "댓글 쓰기",
            description = """
                    채널 내 유저만 호출가능하다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(
            @ApiResponse(
                    responseCode = "201",
                    description = "commentId 반환"
            )
    )
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Long> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentAddRequest request)
    {
        Long commentId = commentCommandApi.createComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }


    @Operation(
            summary = "댓글 삭제",
            description = """
                    글쓴이 혹은 글쓴이 보다 권한이 높은 유저가 호출가능하다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(
            @ApiResponse(
                    responseCode = "204",
                    description = "성공"
            )
    )
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {
        commentCommandApi.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "댓글 조회",
            description = """
                    채널 내 유저만 호출가능하며 post에 종속된 댓글을 전부 가져온다.
                    
                    댓글 생성 시간 기준(DESC) 순서로 가져온다
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentsWithAuthorResponse> getComments(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(commentQueryApi.getComments(postId));
    }
}
