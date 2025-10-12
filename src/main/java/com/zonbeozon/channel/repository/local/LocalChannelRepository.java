package com.zonbeozon.channel.repository.local;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zonbeozon.channel.dto.ChannelInfoDto;
import com.zonbeozon.channel.repository.expression.ChannelConstructorExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zonbeozon.channel.entity.QChannel.channel;
import static com.zonbeozon.channel.entity.QChannelProfile.channelProfile;
import static com.zonbeozon.image.entity.QImage.image;

@Repository
@Transactional
@RequiredArgsConstructor
@Profile("local")
public class LocalChannelRepository {
    private final JPAQueryFactory queryFactory;

    public List<ChannelInfoDto> findAll() {
        JPAQuery<ChannelInfoDto> query =  queryFactory.select(ChannelConstructorExpression.channelInfoDto(
                channel, channelProfile, image
        )).from(channel);

        query.leftJoin(channel.profile, channelProfile).leftJoin(channelProfile.image, image);

        return query.fetch();
    }
}
