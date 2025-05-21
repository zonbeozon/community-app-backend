package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.ChannelContext;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.channel.service.ChannelService;
import com.zonbeozon.channel.service.dto.ChannelResponseWrapper;
import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    @Operation(
            summary = "채널 추가",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "성공",
                    content = @Content(schema = @Schema(type = "integer", format = "int64", description = "채널 ID"))
            ),
            @ApiResponse(responseCode = "400", description = "요청 body가 잘못되었을 때")
    })
    @PostMapping
    public ResponseEntity<Long> addChannel(
            @Valid
            @Parameter(name = "Request Body")
            ChannelCreateRequest channelCreateRequest,
            @Parameter(hidden = true)
            Member member
    ) {
        Long channelId = channelService.addChannel(channelCreateRequest, member);
        return ResponseEntity.ok(channelId);
    }

    @Operation(
            summary = "채널 정보 업데이트",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE + "채널 Owner만 호출 가능",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{channelId}/info")
    public ResponseEntity<Void> updateChannelInfo(
            @Parameter(hidden = true) Member member,
            @Valid ChannelInfoUpdateRequest channelInfoUpdateRequest,
            @PathVariable Long channelId
    ) {
        channelService.updateChannelInfo(ChannelContext.with(channelId, member), channelInfoUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "채널 OpenLevel 변경",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE + "채널 Owner만 호출 가능",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{channelId}/openLevel")
    public ResponseEntity<Void> updateChannelOpenLevel(
            @Parameter(hidden = true) Member member,
            @RequestParam Channel.OpenLevel openLevel,
            @PathVariable Long channelId
    ) {
        channelService.changeOpenLevel(ChannelContext.with(channelId, member), openLevel);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "사용자가 속한 모든 채널 정보 가져오기",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ChannelResponseWrapper.class))),
    })
    @GetMapping("/joined")
    public ResponseEntity<ChannelResponseWrapper> getMemberJoinedChannels(@Parameter(hidden = true) Member member) {
        return ResponseEntity.ok(channelService.createMemberJoinedChannelResponse(member));
    }

    @Operation(
            summary = "채널 검색",
            description = """
                    채널 title중 searchParam이 포함된 채널을 찾는다.
                    정렬 순서는 채널 구독자 DESC순이다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ChannelResponseWrapper.class))),
    })
    @GetMapping
    @Parameters({
            @Parameter(name = "searchParam", description = "채널 명", example = "좋은 채널"),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", example = "0"),
            @Parameter(name = "size", description = "한 페이지당 아이템 수", example = "10")
    })
    public ResponseEntity<ChannelResponseWrapper> getChannels(
            @RequestParam(defaultValue = "") String searchParam,
            @RequestParam(defaultValue = "COMMUNITY_INFO") Channel.Type type,
            @RequestParam(defaultValue = "MEMBER_COUNT") ChannelSort sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
            ) {
        return ResponseEntity.ok(channelService.createChannelSearchResponse(searchParam, page, size, sort, direction, type));
    }

    @Operation(
            summary = "채널 삭제",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE + "채널 Owner만 허용",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(
            @Parameter(hidden = true) Member member,
            @PathVariable Long channelId
            ) {
        channelService.delete(ChannelContext.with(channelId, member));
        return ResponseEntity.noContent().build();
    }


}
