package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;


public interface ChannelRepositoryCustom {
    Page<ChannelWithMemberCount> searchByKeyword(
            String keyword,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            Channel.Type type,
            Channel.OpenLevel openLevel
    );
}
