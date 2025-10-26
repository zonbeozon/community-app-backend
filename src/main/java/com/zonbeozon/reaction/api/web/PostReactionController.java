package com.zonbeozon.reaction.api.web;

import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.reaction.api.PostReactionApi;
import com.zonbeozon.reaction.post.dto.PostReactionCountWithPersonalizedDto;
import com.zonbeozon.reaction.post.entity.ReactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "포스트 리엑션", description = "포스트에 대한 리엑션 엔드포인트")
public class PostReactionController {
    private final PostReactionApi postReactionApi;

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
    @PostMapping("/posts/{postId}/reactions")
    public ResponseEntity<Void> markReaction(
            @PathVariable Long postId,
            @RequestParam ReactionType reactionType
    ) {
        postReactionApi.mark(postId, reactionType);
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
    @DeleteMapping("/posts/{postId}/reactions")
    public ResponseEntity<Void> unmarkReaction(
            @PathVariable Long postId
    ) {
        postReactionApi.unmark(postId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "리엑션 갯수 및 요청자의 리엑션 여부 조회",
            description = """
                    특정 채널 내 여러 게시물(postIds)의 리엑션 집계와
                    API 요청자 본인의 리엑션 여부를 함께 조회
                    """,
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
            )
    })
    @GetMapping("/channels/{channelId}/posts/reactions")
    public ResponseEntity<Map<Long, PostReactionCountWithPersonalizedDto>> getReactionCountsWithPersonalizedInfo(
            @PathVariable Long channelId,
            @RequestParam List<Long> postIds
    ) {
        return ResponseEntity.ok(postReactionApi.getReactionCountsByPostIdIn(channelId, postIds));
    }
}
