package com.zonbeozon.member.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberRemover;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@RequiredArgsConstructor
@Transactional
public class MemberDeleteApi {
    private final MemberRemover memberRemover;
    private final AuthenticationService authenticationService;

    public void deleteMember() {
        Member member = authenticationService.getCurrentMember();
        memberRemover.deleteMember(member.getId());
    }
}
