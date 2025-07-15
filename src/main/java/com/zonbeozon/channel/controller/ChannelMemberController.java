package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.JoinResultStatus;
import com.zonbeozon.channel.service.ChannelMemberJoiner;
import com.zonbeozon.channel.service.ChannelMemberRemover;
import com.zonbeozon.channel.service.ChannelMemberRoleModifier;
import com.zonbeozon.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
            @ApiResponse(responseCode = "204", description = "성공", content = @Content(schema = @Schema())),
    })
    @PatchMapping("/{targetMemberId}/role")
    public ResponseEntity<Void> modifyRole(
            @PathVariable Long channelId,
            @PathVariable Long targetMemberId,
            @RequestParam ChannelRole wantTo
    ) {
        channelMemberRoleModifier.modifyChannelMemberRole(channelId, targetMemberId, wantTo);
        return ResponseEntity.ok().build();
    }


}
