package com.zonbeozon.channel.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.channel.service.ChannelMemberRemover;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class ChannelMemberDeleteApi {
    private final ChannelMemberRemover channelMemberRemover;
    private final AuthenticationService authenticationService;

    public void leaveChannel(Long channelId) {
        Member member = authenticationService.getCurrentMember();
        channelMemberRemover.deleteChannelMember(channelId, member.getId());
    }
}
