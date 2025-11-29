package com.zonbeozon.member.service;

import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.dto.MemberDto;
import com.zonbeozon.member.respository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberAssembler {
    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;

    public MemberDto getMemberResponse(Long memberId) {
        Member member = memberFinder.findByIdWithProfileElseThrow(memberId);
        return MemberDto.from(member);
    }

    public Map<Long, MemberDto> getMemberResponse(Collection<Long> memberIds) {
        List<MemberDto> members = memberRepository.findMemberDtoByIdInWithProfile(memberIds);
        if(memberIds.size() != members.size()) throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        return members.stream().collect(Collectors.toMap(MemberDto::memberId, Function.identity()));
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
