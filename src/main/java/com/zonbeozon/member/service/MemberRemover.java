package com.zonbeozon.member.service;

import com.zonbeozon.auth.service.AuthenticationService;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberRemover {
    private final AuthenticationService authenticationService;
    private final MemberRepository memberRepository;

    /**
     * todo: 맴버를 필드로 들고 있는 엔터티에 대한 처리 필요
     */
    public void deleteMember() {
        Member member = authenticationService.getCurrentMember();
        memberRepository.delete(member);
    }
}
