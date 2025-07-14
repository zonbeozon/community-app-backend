package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.InfoChannelOverview;
import com.zonbeozon.channel.enums.ChannelCreatorType;
import com.zonbeozon.channel.enums.ChannelType;
import com.zonbeozon.member.domain.Member;

import java.util.List;

public interface InfoChannelRepositoryCustom {
    List<InfoChannelOverview> getInfoChannelsByMember(Member member, ChannelCreatorType creatorType);
}
