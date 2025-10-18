package com.zonbeozon.member.service;

import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberCreator {
    private final MemberRepository memberRepository;
    private final MemberProfileService memberProfileService;

    public Member createMember(String username, String email, ServerRole role) {
        if(isExistEmail(email)) throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
        if(isExistUsername(username)) throw new ConflictException(ErrorCode.DUPLICATE_USERNAME);
        Member member = new Member(username, email, role);
        memberRepository.save(member);
        memberProfileService.setAsDefaultProfile(member.getId());
        return member;
    }

    public Member createMemberWithRandomUsername(String email, ServerRole role) {
        String username = UUIDUsernameGenerator.generate();
        return createMember(username, email, role);
    }

    private boolean isExistUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    private boolean isExistEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
