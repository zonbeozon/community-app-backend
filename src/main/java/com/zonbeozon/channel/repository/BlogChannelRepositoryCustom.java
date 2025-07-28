package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.JoinedBlogChannelOverview;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.member.domain.Member;

import java.util.List;

public interface BlogChannelRepositoryCustom {
    List<JoinedBlogChannelOverview> getBlogChannelsByMember(Member member, ChannelCreatorType creatorType);
}
