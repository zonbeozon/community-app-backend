package com.zonbeozon.channel.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.ChannelInfoDto;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.repository.expression.ChannelConstructorExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zonbeozon.channel.entity.QChannel.channel;
import static com.zonbeozon.channel.entity.QChannelProfile.channelProfile;
import static com.zonbeozon.image.entity.QImage.image;

@Repository
@RequiredArgsConstructor
class ChannelRepositoryImpl implements ChannelRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Channel> findByIdWithProfile(Long channelId) {
        JPAQuery<Channel> query = queryFactory.selectFrom(channel);

        query.leftJoin(channel.profile, channelProfile).leftJoin(channelProfile.image, image);

        query.where(channel.id.eq(channelId));

        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public Optional<ChannelInfoDto> findByIdWithProfileAndChannelMemberCount(Long channelId) {
        JPAQuery<ChannelInfoDto> query = queryFactory.select(
                ChannelConstructorExpression.channelInfoDto(channel, channelProfile, image)
                )
                .from(channel);

        query.leftJoin(channel.profile, channelProfile).leftJoin(channelProfile.image, image);

        query.where(channel.id.eq(channelId));

        return Optional.ofNullable(query.fetchOne());
    }

    @Override
    public List<ChannelInfoDto> findByIdInWithProfileAndChannelMemberCount(List<Long> channelIds) {
        JPAQuery<ChannelInfoDto> query = queryFactory.select(
                        ChannelConstructorExpression.channelInfoDto(channel, channelProfile, image)
                )
                .from(channel);

        query.leftJoin(channel.profile, channelProfile).leftJoin(channelProfile.image, image);

        query.where(channel.id.in(channelIds));

        return query.fetch();
    }
}
