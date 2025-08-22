package com.zonbeozon.reaction.controller;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.post.service.PostAuthorizationCheckService;
import com.zonbeozon.reaction.enums.ReactionContentType;
import com.zonbeozon.reaction.enums.ReactionType;
import com.zonbeozon.reaction.service.ReactionMarker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "포스트 리엑션", description = "포스트에 대한 리엑션 엔드포인트")
@RequestMapping("/post/{postId}/reaction")
public class PostReactionController {
    private final ReactionMarker reactionMarker;
    private final PostAuthorizationCheckService postAuthorizationCheckService;

    @Operation(
            summary = "리엑션 생성",
            description = """
                    POST에 관한 리엑션을 추가한다.
                    
                    해당 POST에 대해 기존에 리엑션이 존재한다면 기존 리엑션을 삭제 후 새로운 리엑션으로 대체한다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "성공 - 응답 바디 없음"
            )
    })
    @PostMapping
    public ResponseEntity<Void> markReaction(
            @PathVariable Long postId,
            @RequestParam ReactionType reactionType
    ) {
        if(!postAuthorizationCheckService.isAtLeastMember(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        reactionMarker.mark(postId, ReactionContentType.POST, reactionType);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "리엑션 삭제",
            description = """
                    작성자의 해당 POST에 관한 리엑션을 삭제한다.
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "성공 - 응답 바디 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "작성자가 기존에 해당 POST에 대해 리엑션을 하지 않은 상태라면"
            )
    })
    @DeleteMapping
    public ResponseEntity<Void> unmarkReaction(
            @PathVariable Long postId
    ) {
        if(!postAuthorizationCheckService.isAtLeastMember(postId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        reactionMarker.unmark(postId, ReactionContentType.POST);
        return ResponseEntity.noContent().build();
    }
}
