package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.JoinResultStatus;
import com.zonbeozon.channel.service.ChannelAuthorizationCheckService;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.channel.service.ChannelMemberJoiner;
import com.zonbeozon.channel.service.ChannelMemberRemover;
import com.zonbeozon.channel.service.ChannelMemberRoleModifier;
import com.zonbeozon.global.SortExcludedPageRequest;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel/{channelId}/member")
@Tag(name = "채널 맴버", description = "채널에 속해 있는 맴버 관련 엔드포인트.")
public class ChannelMemberController {
    private final ChannelMemberJoiner channelMemberJoiner;
    private final ChannelMemberRemover channelMemberRemover;
    private final ChannelMemberRoleModifier channelMemberRoleModifier;
    private final ChannelMemberAssembler channelMemberAssembler;
    private final ChannelAuthorizationCheckService channelAuthorizationCheckService;

    @Operation(
            summary = "채널 참가",
            description = "채널의 OpenLevel이 public일때만 가입 가능",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "202", description = "요청 발송됨", content = @Content(schema = @Schema())),
    })
    @PostMapping
    public ResponseEntity<Void> joinChannelAsMember(
            @PathVariable Long channelId
    ) {
        JoinResultStatus status = channelMemberJoiner.joinAsMember(channelId);

        if(status == JoinResultStatus.APPROVAL_REQUESTED)
            return ResponseEntity.accepted().build();

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "채널 유저 강퇴",
            description = "강퇴시킬려는 유저가 당하는 유저보다 ChannelRole이 높아야 한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema())),
    })
    @DeleteMapping("/{targetMemberId}/kick")
    public ResponseEntity<Void> kickChannelMember(
            @PathVariable Long channelId,
            @PathVariable Long targetMemberId
    ) {
        if(!channelAuthorizationCheckService.hasHigherRoleThanTargetMember(channelId, targetMemberId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberRemover.kickMember(channelId, targetMemberId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "채널 떠나기",
            description = "만일 유저의 채널Role이 Owner라면 이전시키기 전에는 떠날 수 없다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "유저가 Owner일때", content = @Content(schema = @Schema())),
    })
    @DeleteMapping
    public ResponseEntity<Void> leaveChannel(
            @PathVariable Long channelId
    ) {
        channelMemberRemover.leaveChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "채널 맴버 Role 변경",
            description =
                    "Owner만 호출가능하다\n" +
                    "만일 변경시키고자하는 Role이 Owner라면 자신의 Owner Role이 이전되고 자신은 Admin으로 강등된다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema())),
    })
    @PatchMapping("/{targetMemberId}/role")
    public ResponseEntity<Void> modifyRole(
            @PathVariable Long channelId,
            @PathVariable Long targetMemberId,
            @RequestParam ChannelRole wantTo
    ) {
        if(!channelAuthorizationCheckService.isOwner(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberRoleModifier.modifyChannelMemberRole(channelId, targetMemberId, wantTo);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "활성 채널 맴버 조회",
            description = """
                    활성화된 채널 맴버를 조회한다.
                    
                    채널에 속해있는 활성맴버만 호출가능하다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema())),
    })
    @GetMapping
    public ResponseEntity<Page<ChannelMemberResponse>> getActiveChannelMembers(
            @PathVariable Long channelId,
            SortExcludedPageRequest pageRequest
    ) {
        if(!channelAuthorizationCheckService.isAtLeastMember(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        return ResponseEntity.ok(
                channelMemberAssembler.createPagedActiveChannelMemberResponse(channelId, pageRequest)
        );
    }

    @Operation(
            summary = "강퇴된 맴버 조회",
            description = """
                    Owner만 호출가능하다.
                    강제퇴장된 맴버를 조회한다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/kicked")
    public ResponseEntity<Page<ChannelMemberResponse>> getKickedChannelMembers(
            @PathVariable Long channelId,
            SortExcludedPageRequest pageRequest
    ) {
        if(channelAuthorizationCheckService.isOwner(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);

        return ResponseEntity.ok(
                channelMemberAssembler.createPagedKickedChannelMemberResponse(channelId, pageRequest)
        );
    }


}
