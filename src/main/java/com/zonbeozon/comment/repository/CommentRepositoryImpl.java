package com.zonbeozon.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.repository.expression.ChannelConstructorExpression;
import com.zonbeozon.comment.dto.CommentDto;
import com.zonbeozon.comment.dto.CommentWithAuthorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.zonbeozon.channel.entity.QBlogChannel.blogChannel;
import static com.zonbeozon.channel.entity.QChannelMember.channelMember;
import static com.zonbeozon.comment.entity.QComment.comment;
import static com.zonbeozon.image.entity.QImage.image;
import static com.zonbeozon.member.domain.QMember.member;
import static com.zonbeozon.member.domain.QMemberProfile.memberProfile;
import static com.zonbeozon.post.domain.QPost.post;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentDto> findCommentDtoByPostIdWOrderByCreatedAtDesc(Long postId) {
        return queryFactory
                .select(Projections.constructor(CommentDto.class,
                        comment.id,
                        comment.content,
                        member.id,
                        comment.createdAt
                ))
                .from(comment)
                .join(comment.author, member)
                .where(comment.post.id.eq(postId))
                .orderBy(comment.createdAt.desc())
                .fetch();
    }

    @Override
    public Optional<CommentWithAuthorResponse> findCommentWithAuthorByCommentId(Long commentId) {
        CommentWithAuthorResponse result = queryFactory.select(Projections.constructor(CommentWithAuthorResponse.class,
                comment.id,
                comment.content,
                ChannelConstructorExpression.channelMemberDto(member, channelMember, memberProfile, image),
                comment.createdAt
                ))
                .from(comment)
                .join(comment.author, member)
                .leftJoin(member.profile, memberProfile)
                .leftJoin(memberProfile.image, image)
                .join(comment.post, post)
                .join(post.channel, blogChannel)
                .join(channelMember).on(channelMember.member.eq(member).and(channelMember.channel.id.eq(blogChannel.id)))
                .where(comment.id.eq(commentId))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
