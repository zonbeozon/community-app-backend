package com.zonbeozon.member.api;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.global.annotation.ApiComponent;
import com.zonbeozon.image.service.ImageOwnershipVerifier;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.dto.MemberDto;
import com.zonbeozon.member.dto.MemberProfileUpdateRequest;
import com.zonbeozon.member.dto.UsernameUpdateRequest;
import com.zonbeozon.member.service.MemberAssembler;
import com.zonbeozon.member.service.MemberUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApiComponent
@Transactional
@RequiredArgsConstructor
public class MemberUpdateApi {
    private final MemberUpdater memberUpdater;
    private final AuthenticationService authenticationService;
    private final MemberAssembler memberAssembler;
    private final ImageOwnershipVerifier imageOwnershipVerifier;

    public MemberDto updateUsername(UsernameUpdateRequest request) {
        Member member = authenticationService.getCurrentMember();
        memberUpdater.updateUsername(member.getId(), request.username());
        return memberAssembler.getMemberResponse(member.getId());
    }

    public MemberDto updateProfile(MemberProfileUpdateRequest request) {
        Member member = authenticationService.getCurrentMember();
        imageOwnershipVerifier.verify(member.getId(), request.imageId());
        memberUpdater.updateProfile(member.getId(), request.imageId());
        return memberAssembler.getMemberResponse(member.getId());
    }
}
