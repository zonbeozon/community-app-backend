package com.zonbeozon.member.service;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.respository.MemberRepository;
import com.zonbeozon.member.respository.MemberSort;
import com.zonbeozon.member.service.dto.MemberResponse;
import com.zonbeozon.member.service.dto.PagedMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberAssembler {
    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;

    public MemberResponse createMemberResponse(Long memberId) {
        Member member = memberFinder.findByIdElseThrow(memberId);
        return MemberResponse.from(member);
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
}
