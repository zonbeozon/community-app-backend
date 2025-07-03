package com.zonbeozon.member.respository;

import com.zonbeozon.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface MemberRepositoryCustom {
    Page<Member> searchMemberByPartialUsername(
            String partialUsername,
            MemberSort sort,
            Sort.Direction direction,
            int page,
            int size
    );
}
