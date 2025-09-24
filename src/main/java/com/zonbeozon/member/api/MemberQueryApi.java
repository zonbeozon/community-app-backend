package com.zonbeozon.member.api;

import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.member.dto.MemberDto;
import com.zonbeozon.member.service.MemberAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ApiComponent
@RequiredArgsConstructor
public class MemberQueryApi {
    private final MemberAssembler memberAssembler;

    public MemberDto getMemberResponse(Long memberId) {
        return memberAssembler.getMemberResponse(memberId);
    }

    public Page<MemberDto> getMemberResponseByUsername(Pageable pageable, String username) {
        return memberAssembler.getMemberResponseByUsername(username, pageable);
    }
}
