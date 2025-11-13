package com.zonbeozon.member.respository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.image.dto.ImageDto;
import com.zonbeozon.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.zonbeozon.image.entity.QImage.image;
import static com.zonbeozon.member.domain.QMember.member;
import static com.zonbeozon.member.domain.QMemberProfile.memberProfile;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberDto> findMemberDtoByIdInWithProfile(Collection<Long> ids) {
        return queryFactory.select(Projections.constructor(MemberDto.class,
                member.id,
                member.username,
                image.id,
                image.url,
                member.role
                ))
                .from(member)
                .leftJoin(member.profile, memberProfile)
                .leftJoin(memberProfile.image, image)
                .where(member.id.in(ids))
                .fetch();
    }
}
