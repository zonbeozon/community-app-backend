package com.zonbeozon.channel.controller;

public class ChannelInvitationController {
//    @Operation(
//            summary = "채널 초대 발송",
//            description = SwaggerConfig.NEED_TO_AUTH_MESSAGE +
//                    "채널 Invite는 Admin부터 가능하다\n" +
//                    "채널의 JoinLevel이 Open이거나 InviteOnly이여야 한다.\n" +
//                    "초대가 성공적이라면 초대를 보낸사람에게는 200 ok, 빈 responseBody가 가고.\n" +
//                    "초대를 받은 당사자에게는 Nofitication이 간다",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(contentSchema = InviteCodeResponse.class))),
//    })
//    @PatchMapping("/{channelId}/")
//    public ResponseEntity<Void> modifyRole(
//            @Parameter(hidden = true) Member member,
//            @PathVariable Long channelId,
//            @RequestParam Long targetChannelMemberId,
//            @RequestParam ChannelRole wantTo
//    ) {
//        channelMemberService.modifyChannelMemberRole(member, channelId, targetChannelMemberId, wantTo);
//        return ResponseEntity.ok().build();
//    }
}
