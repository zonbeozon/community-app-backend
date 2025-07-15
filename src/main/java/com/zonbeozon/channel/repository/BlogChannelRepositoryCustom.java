package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.BlogChannelOverview;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.member.domain.Member;

import java.util.List;

public interface BlogChannelRepositoryCustom {
    List<BlogChannelOverview> getBlogChannelsByMember(Member member, ChannelCreatorType creatorType);
}
