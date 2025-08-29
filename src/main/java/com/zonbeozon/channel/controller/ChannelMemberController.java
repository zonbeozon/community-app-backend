package com.zonbeozon.channel.controller;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.dto.ChannelMemberResponse;
import com.zonbeozon.channel.entity.ChannelMemberId;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.JoinResultStatus;
import com.zonbeozon.channel.service.*;
import com.zonbeozon.channel.service.assembler.ChannelMemberAssembler;
import com.zonbeozon.global.SortExcludedPageRequest;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
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
    private final ChannelMemberBanService channelMemberBanService;
    private final AuthenticationService authenticationService;

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
            summary = "채널 유저 벤",
            description = "벤시킬려는 유저가 당하는 유저보다 ChannelRole이 높아야 한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema())),
    })
    @PostMapping("/{memberId}/ban")
    public ResponseEntity<Void> banChannelMember(
            @PathVariable Long channelId,
            @PathVariable Long memberId
    ) {
        if(!channelAuthorizationCheckService.hasHigherRoleThanTargetMember(channelId, memberId))
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberBanService.ban(new ChannelMemberId(channelId, memberId));
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
        Member member = authenticationService.getCurrentMember();
        channelMemberRemover.leaveChannel(new ChannelMemberId(channelId, member.getId()));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "채널 맴버 Role 변경",
            description = """
                    Owner만 호출가능하다
                    
                    만일 변경시키고자하는 Role이 Owner라면 자신의 Owner Role이 이전되고 자신은 Admin으로 강등된다.
                    """,
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
                channelMemberAssembler.getPagedActiveChannelMemberResponse(channelId, pageRequest)
        );
    }

    @Operation(
            summary = "참가 승인 대기 상태인 맴버 조회",
            description = """
                    Admin이상 부터 호출가능하다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/pending")
    public ResponseEntity<Page<ChannelMemberResponse>> getPendingChannelMembers(
            @PathVariable Long channelId,
            SortExcludedPageRequest pageRequest
    ) {
        if(!channelAuthorizationCheckService.isAtLeastAdmin(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);

        return ResponseEntity.ok(
                channelMemberAssembler.getPagedPendingChannelMemberResponse(channelId, pageRequest)
        );
    }

    @Operation(
            summary = "벤 상태인 맴버 벤 해제",
            description = """
                    Owner만 호출가능하다.
                    
                    벤을 해제한다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공"),
    })
    @DeleteMapping("/{memberId}/ban")
    public ResponseEntity<Page<ChannelMemberResponse>> unbanChannelMember(
            @PathVariable Long channelId,
            @PathVariable Long memberId
    ) {
        if(!channelAuthorizationCheckService.isOwner(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberBanService.unban(new ChannelMemberId(channelId, memberId));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "벤 상태인 맴버 조회",
            description = """
                    Owner만 호출가능하다.
                    
                    벤 상태인 맴버를 조회한다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/ban")
    public ResponseEntity<Page<ChannelMemberResponse>> getBannedChannelMembers(
            @PathVariable Long channelId,
            SortExcludedPageRequest pageRequest
    ) {
        if(!channelAuthorizationCheckService.isOwner(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);

        return ResponseEntity.ok(
                channelMemberAssembler.getPagedBannedChannelMemberResponse(channelId, pageRequest)
        );
    }

    @Operation(
            summary = "참가 승인 대기중인 맴버 승인",
            description = """
                    Admin 이상 부터 호출가능하다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{memberId}/approve")
    public ResponseEntity<Void> approveJoinRequest(
            @PathVariable Long channelId,
            @PathVariable Long memberId
    ) {
        if(!channelAuthorizationCheckService.isAtLeastAdmin(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberJoiner.approveJoinRequest(new ChannelMemberId(channelId, memberId));
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "참가 승인 대기중인 맴버 거절",
            description = """
                    Admin 이상 부터 호출가능하다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공"),
    })
    @PostMapping("/{memberId}/deny")
    public ResponseEntity<Void> denyJoinRequest(
            @PathVariable Long channelId,
            @PathVariable Long memberId
    ) {
        if(!channelAuthorizationCheckService.isAtLeastMember(channelId)) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        channelMemberJoiner.denyJoinRequest(new ChannelMemberId(channelId, memberId));
        return ResponseEntity.noContent().build();
    }
}
