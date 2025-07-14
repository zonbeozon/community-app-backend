package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.dto.ChannelWithMemberCount;
import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ChannelRepositoryCustom {
    Page<ChannelWithMemberCount> searchByKeyword(
            String keyword,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            ChannelType type,
            ChannelContentVisibility contentVisibility,
            ChannelJoinPolicy joinPolicy
    );
}
