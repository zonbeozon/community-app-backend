package com.zonbeozon.channel.controller;

import com.zonbeozon.channel.ChannelContext;
import com.zonbeozon.channel.service.ChannelMemberService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channel")
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
    @PostMapping("/{channelId}/join")
    public ResponseEntity<Void> joinChannelAsMember(
            @PathVariable Long channelId,
            @Parameter(hidden = true) Member member
    ) {
        channelMemberService.joinAsMember(ChannelContext.with(channelId), member);
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
    @PostMapping("/{channelId}/kick/{targetId}")
    public ResponseEntity<Void> kickChannelMember(
            @Parameter(hidden = true) Member member,
            @PathVariable Long channelId,
            @PathVariable Long targetId
    ) {
        channelMemberService.kickMember(ChannelContext.with(channelId, member), targetId);
        return ResponseEntity.ok().build();
    }


}
