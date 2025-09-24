package com.zonbeozon.member.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.respository.MemberRepository;
import com.zonbeozon.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberAssembler {
    private final MemberFinder memberFinder;

    public MemberDto getMemberResponse(Long memberId) {
        Member member = memberFinder.findByIdWithProfileElseThrow(memberId);
        return MemberDto.from(member);
    }

    /**
     * todo: 검색 도입
     */
    public Page<MemberDto> getMemberResponseByUsername(
            String username,
            Pageable pageable
    ) {
        throw new UnsupportedOperationException("해당 기능은 미구현입니다");
    }
}
