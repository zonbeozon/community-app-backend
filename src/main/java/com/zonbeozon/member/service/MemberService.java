package com.zonbeozon.member.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.exception.MemberBadRequestException;
import com.zonbeozon.member.respository.MemberRepository;
import com.zonbeozon.member.respository.MemberSort;
import com.zonbeozon.member.service.dto.MemberResponse;
import com.zonbeozon.member.exception.MemberException;
import com.zonbeozon.member.exception.MemberNotFoundException;
import com.zonbeozon.member.service.dto.PagedMemberResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public Member getByEmailOrElseThrow(String email) {
        return getByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email + "을 가진 member는 존재하지 않습니다."));
    }

    public Optional<Member> getByEmail(String email) {
        return memberRepository.findByEmail(email);

    }

    public Member getByIdOrThrow(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id + "는 존재하지 않는 memberId 입니다."));
    }

    @Transactional
    public Member createMemberWithRandomUsername(String email, String profile, ServerRole role) {
        String username = UUIDUsernameGenerator.generate();
        return createMember(username, email, profile, role);
    }

    @Transactional
    public Member createMember(String username, String email, String profile, ServerRole role) {
        if(isExistEmail(email)) throw new MemberException(email + "는 이미 존재하는 이메일입니다.");
        if(isExistUsername(username)) throw new MemberException(username + "는 이미 존재하는 username 입니다.");
        Member member = new Member(username, email, profile, role);
        memberRepository.save(member);
        return member;
    }

    @Transactional
    public void updateUsername(Member member , String newUsername) {
        if(isExistUsername(newUsername))
            throw new MemberBadRequestException(MemberBadRequestException.ErrorCode.DUPLICATE_USERNAME);
        member.updateUsername(newUsername);
    }

    public PagedMemberResponse searchPagedMemberResponse(
            String partialUsername,
            MemberSort sort,
            Sort.Direction direction,
            int page,
            int size
    ) {
        Page<Member> members = memberRepository.searchMemberByPartialUsername(partialUsername, sort, direction, page, size);
        return PagedMemberResponse.from(members);
    }

    public MemberResponse getMemberResponse(Long memberId) {
        Member member = getByIdOrThrow(memberId);
        return MemberResponse.from(member);
    }

    /**
     * todo: 맴버를 필드로 들고 있는 엔터티에 대한 처리 필요
     */
    @Transactional
    public void deleteMember(Member member) {
        getByIdOrThrow(member.getId()).deleteMember();
    }

    private boolean isExistUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    private boolean isExistEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
