package com.zonbeozon.member.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.exception.MemberException;
import com.zonbeozon.member.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberCreator {
    private final MemberRepository memberRepository;

    public Member createMember(String username, String email, String profile, ServerRole role) {
        if(isExistEmail(email)) throw new MemberException(email + "는 이미 존재하는 이메일입니다.");
        if(isExistUsername(username)) throw new MemberException(username + "는 이미 존재하는 username 입니다.");
        Member member = new Member(username, email, profile, role);
        memberRepository.save(member);
        return member;
    }


    @Transactional
    public Member createMemberWithRandomUsername(String email, String profile, ServerRole role) {
        String username = UUIDUsernameGenerator.generate();
        return createMember(username, email, profile, role);
    }

    private boolean isExistUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    private boolean isExistEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
