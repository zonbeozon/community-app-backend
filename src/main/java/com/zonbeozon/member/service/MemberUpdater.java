package com.zonbeozon.member.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.exception.MemberBadRequestException;
import com.zonbeozon.member.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberUpdater {
    private final AuthenticationService authenticationService;
    private final MemberRepository memberRepository;

    public void updateUsername(String newUsername) {
        Member member = authenticationService.getCurrentMember();
        if(isExistUsername(newUsername))
            throw new MemberBadRequestException(MemberBadRequestException.ErrorCode.DUPLICATE_USERNAME);
        member.updateUsername(newUsername);
    }

    private boolean isExistUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

}
