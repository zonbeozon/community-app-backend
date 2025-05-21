package com.zonbeozon.channel.service.dto;

import com.zonbeozon.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InviteCodeResponse {
    private String code;
    private ChannelResponse channel;
    private MemberResponse inviter;

}
