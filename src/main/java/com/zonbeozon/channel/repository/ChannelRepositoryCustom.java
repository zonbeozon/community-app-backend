package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ChannelRepositoryCustom {
    Page<ChannelWithMemberCount> searchByKeyword(
            String keyword,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            ChannelType type,
            ChannelContentOpenLevel contentOpenLevel,
            ChannelJoinLevel joinLevel
    );

    List<JoinedChannelDto> findJoinedChannels(Member member);
}
