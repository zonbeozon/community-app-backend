package com.zonbeozon.member.respository;

import com.zonbeozon.member.dto.MemberDto;

import java.util.Collection;
import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberDto> findMemberDtoByIdInWithProfile(Collection<Long> ids);
}
