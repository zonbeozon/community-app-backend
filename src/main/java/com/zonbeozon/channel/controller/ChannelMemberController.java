package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.entity.ChannelRole;
import com.zonbeozon.channel.service.ChannelMemberService;
import com.zonbeozon.channel.service.dto.InviteCodeResponse;
import com.zonbeozon.config.SwaggerConfig;
import com.zonbeozon.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel/{channelId}/member")
public class ChannelMemberController {
    private final ChannelMemberService channelMemberService;

    @Operation(
            summary = "채널 참가",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE + "채널의 OpenLevel이 public일때만 가입 가능",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema())),
    })
    @PostMapping
    public ResponseEntity<Void> joinChannelAsMember(
            @PathVariable Long channelId,
            @Parameter(hidden = true) Member member
    ) {
        channelMemberService.joinAsMember(member, channelId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "채널 유저 강퇴",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE + "강퇴시킬려는 유저가 당하는 유저보다 ChannelRole이 높아야 한다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(schema = @Schema())),
    })
    @DeleteMapping("/{targetChannelMemberId}/kick")
    public ResponseEntity<Void> kickChannelMember(
            @Parameter(hidden = true) Member member,
            @PathVariable Long channelId,
            @PathVariable Long targetChannelMemberId
    ) {
        channelMemberService.kickMember(member, channelId, targetChannelMemberId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "채널 떠나기",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE + "만일 유저의 채널Role이 Owner라면 이전시키기 전에는 떠날 수 없다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "유저가 Owner일때", content = @Content(schema = @Schema())),
    })
    @DeleteMapping
    public ResponseEntity<Void> leaveChannel(
            @Parameter(hidden = true) Member member,
            @PathVariable Long channelId
    ) {
        channelMemberService.leaveChannel(member, channelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "채널 맴버 Role 변경",
            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE +
                    "Owner만 호출가능하다\n" +
                    "만일 변경시키고자하는 Role이 Owner라면 자신의 Owner Role이 이전되고 자신은 Admin으로 강등된다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "성공", content = @Content(schema = @Schema())),
    })
    @PatchMapping("/{targetChannelMemberId}/role")
    public ResponseEntity<Void> modifyRole(
            @Parameter(hidden = true) Member member,
            @PathVariable Long channelId,
            @PathVariable Long targetChannelMemberId,
            @RequestParam ChannelRole wantTo
    ) {
        channelMemberService.modifyChannelMemberRole(member, channelId, targetChannelMemberId, wantTo);
        return ResponseEntity.ok().build();
    }


}
