package com.zonbeozon.channel.service;

import com.zonbeozon.channel.controller.ChannelUpdateRequest;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.channel.service.dto.JoinedChannelListResponse;
import com.zonbeozon.channel.service.dto.PagedChannelResponse;
import com.zonbeozon.member.domain.Member;
import org.springframework.data.domain.Sort;

public interface ChannelService {
    Long addChannel(ChannelCreateCommand command, Member requester);
    void updateChannel(Member member, Long channelId, ChannelUpdateRequest channelUpdateRequest);
    void deleteChannel(Member member, Long channelId);
    JoinedChannelListResponse createMemberJoinedChannelResponse(Member member);
    PagedChannelResponse createChannelSearchResponse(
            String searchParam,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            ChannelType type,
            ChannelContentOpenLevel contentOpenLevel,
            ChannelJoinLevel joinLevel
    );
}
