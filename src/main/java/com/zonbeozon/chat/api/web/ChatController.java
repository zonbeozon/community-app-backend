package com.zonbeozon.chat.api.web;

import com.zonbeozon.chat.api.ChatCommendApi;
import com.zonbeozon.chat.api.ChatQueryApi;
import com.zonbeozon.chat.domain.ChatCursor;
import com.zonbeozon.chat.dto.*;
import com.zonbeozon.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RestController
@RequiredArgsConstructor
@Tag(name = "채팅", description = "채팅 관련 엔드포인트")
public class ChatController {
    private final ChatCommendApi chatCommendApi;
    private final ChatQueryApi chatQueryApi;

    @Operation(
            summary = "채팅 생성(그룹 이름)",
            description = "채팅 길이는 512자 이내, 채팅 이미지는 3개 까지 가능하다.",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            ))
    })
    @PostMapping("chattingGroups/{chattingGroupName}/chats")
    public ResponseEntity<Long> createChat(
            @PathVariable
            String chattingGroupName,
            @RequestBody
            @Valid
            ChatCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatCommendApi.createChat(chattingGroupName, request));
    }

    @Operation(
            summary = "채팅 내용 업데이트",
            description = "작성자만 가능",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @PatchMapping("chats/{chatId}")
    public ResponseEntity<Long> updateChatContent(
            @PathVariable
            Long chatId,
            @RequestBody
            @Valid
            ChatContentUpdateRequest request
    ) {
        chatCommendApi.updateContent(chatId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "채팅 이미지 추가",
            description = "작성자만 가능",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공")
    })
    @PostMapping("chats/{chatId}/images")
    public ResponseEntity<Long> addChatImages(
            @PathVariable
            Long chatId,
            @RequestBody
            @Valid
            ChatImagesAddRequest request
    ) {
        chatCommendApi.addChatImages(chatId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "채팅 이미지 삭제",
            description = "작성자만 가능",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공")
    })
    @DeleteMapping("chats/{chatId}/images")
    public ResponseEntity<Long> deleteChatImages(
            @PathVariable
            Long chatId,
            @Valid
            @RequestBody ChatImagesDeleteRequest request
    ) {
        chatCommendApi.deleteChatImages(chatId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "채팅 삭제",
            description = "작성자만 삭제가능",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공")
    })
    @DeleteMapping("chats/{chatId}")
    public ResponseEntity<Long> deleteChat(
            @PathVariable
            Long chatId
    ) {
        chatCommendApi.deleteChat(chatId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "채팅 조회(그룹 이름)",
            description = "생성일 순으로 응답",
            security = @SecurityRequirement(name = SwaggerConfig.SECURITY_METHOD)
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(
                    schema = @Schema(implementation = PagedChatPayload.class))
            ),
    })
    @GetMapping("chattingGroups/{chattingGroupName}/chats")
    public ResponseEntity<PagedChatPayload> getPagedChat(
            @PathVariable
            String chattingGroupName,
            @RequestParam
            ChatCursor cursor,
            @RequestParam(required = false, defaultValue = "20")
            int pageSize
    ) {
        return ResponseEntity.ok(chatQueryApi.getPagedChatPayload(chattingGroupName, cursor, pageSize));
    }
}
