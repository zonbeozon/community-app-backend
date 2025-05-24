package com.zonbeozon.channel.service;

import com.zonbeozon.channel.controller.ChannelInfoUpdateRequest;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.channel.service.dto.JoinedChannelResponseWrapper;
import com.zonbeozon.channel.service.dto.SearchChannelResponseWrapper;
import com.zonbeozon.member.domain.Member;
import org.springframework.data.domain.Sort;

public interface ChannelService {
    Long addChannel(ChannelCreateCommand command, Member requester);
    void updateChannelInfo(Member member, Long channelId, ChannelInfoUpdateRequest channelInfoUpdateRequest);
    void deleteChannel(Member member, Long channelId);
    void changeContentOpenLevel(Member member, Long channelId, ChannelContentOpenLevel openLevel);
    JoinedChannelResponseWrapper createMemberJoinedChannelResponse(Member member);
    SearchChannelResponseWrapper createChannelSearchResponse(
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
