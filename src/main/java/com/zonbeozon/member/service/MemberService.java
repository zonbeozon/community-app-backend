package com.zonbeozon.member.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.respository.MemberRepository;
import com.zonbeozon.member.dto.MemberResponse;
import com.zonbeozon.member.exception.MemberException;
import com.zonbeozon.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Optional<Member> getByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Member getByIdOrThrow(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id + "는 존재하지 않는 memberId 입니다."));
    }

    @Transactional
    public Member createMemberWithRandomUsername(String email, String profile, ServerRole role) {
        if(isExistEmail(email)) throw new MemberException(email + "는 이미 존재하는 이메일입니다.");
        String username = UUIDUsernameGenerator.generate();
        if(isExistUsername(username)) throw new MemberException(username + "는 이미 존재하는 username 입니다.");
        return create(username, email, profile, role);
    }

    private Member create(String username, String email, String profile, ServerRole role) {
        Member member = new Member(username, email, profile, role);
        memberRepository.save(member);
        return member;
    }

    public MemberResponse createMemberResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getUsername(),
                member.getProfile(),
                member.getRole()
        );
    }

    private boolean isExistUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    private boolean isExistEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
