package com.zonbeozon.channel.service;

import com.zonbeozon.channel.controller.ChannelInfoUpdateRequest;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.repository.ChannelSort;
import com.zonbeozon.channel.service.dto.InviteCodeResponse;
import com.zonbeozon.channel.service.dto.JoinedChannelResponseWrapper;
import com.zonbeozon.channel.service.dto.SearchChannelResponseWrapper;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class ChannelRelatedServiceAdapter implements ChannelService, ChannelMemberService, ChannelInvitationService {
    private final ChannelServiceImpl channelServiceImpl;
    private final ChannelMemberServiceImpl channelMemberServiceImpl;
    private final ChannelInvitationServiceImpl channelInvitationServiceImpl;
    private final ChannelMemberResolver channelMemberResolver;

    @Override
    public Long addChannel(ChannelCreateCommand command, Member requester) {
        return channelServiceImpl.addChannel(command, requester);
    }

    @Override
    public void updateChannelInfo(Member member, Long channelId, ChannelInfoUpdateRequest channelInfoUpdateRequest) {
        channelMemberResolver.findChannelMemberThenConsume(member, channelId,
                channelMember -> channelServiceImpl.updateChannelInfo(channelMember, channelInfoUpdateRequest)
        );
    }

    @Override
    public void deleteChannel(Member member, Long channelId) {
        channelMemberResolver.findChannelMemberThenConsume(member, channelId, channelServiceImpl::deleteChannel);
    }

    @Override
    public void changeContentOpenLevel(Member member, Long channelId, ChannelContentOpenLevel openLevel) {
        channelMemberResolver.findChannelMemberThenConsume(member, channelId,
                channelMember -> channelServiceImpl.changeContentOpenLevel(channelMember, openLevel)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public JoinedChannelResponseWrapper createMemberJoinedChannelResponse(Member member) {
        return channelServiceImpl.createMemberJoinedChannelResponse(member);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchChannelResponseWrapper createChannelSearchResponse(
            String searchParam,
            int page,
            int size,
            ChannelSort sort,
            Sort.Direction direction,
            ChannelType type,
            ChannelContentOpenLevel contentOpenLevel,
            ChannelJoinLevel joinLevel
    ) {
        return channelServiceImpl.createChannelSearchResponse(searchParam, page, size, sort, direction, type, contentOpenLevel, joinLevel);
    }

    @Override
    public void joinAsMember(Member member, Long channelId) {
        Channel channel = channelServiceImpl.getChannelByIdOrThrow(channelId);
        channelMemberServiceImpl.joinAsMember(member, channel);
    }

    @Override
    public void kickMember(Member member, Long channelId, Long targetChannelMemberId) {
        channelMemberResolver.findChannelMemberThenConsume(member, channelId, actor -> {
            ChannelMember target = channelMemberServiceImpl.getByIdOrThrow(targetChannelMemberId);
            channelMemberServiceImpl.kickMember(actor, target);
        });
    }

    @Override
    public void modifyChannelMemberRole(Member member, Long channelId, Long targetChannelMemberId, ChannelRole wantToChange) {
        channelMemberResolver.findChannelMemberThenConsume(member, channelId, actor -> {
            ChannelMember target = channelMemberServiceImpl.getByIdOrThrow(targetChannelMemberId);
            channelMemberServiceImpl.modifyChannelMemberRole(actor, target, wantToChange);
        });
    }

    @Override
    public void leaveChannel(Member member, Long channelId) {
        channelMemberResolver.findChannelMemberThenConsume(member, channelId, channelMemberServiceImpl::leaveChannel);
    }

    @Override
    public InviteCodeResponse publishInvite(Member member, Long channelId, Long inviteeId) {
        return channelMemberResolver.findChannelMemberThenApply(member, channelId,
                channelMember -> channelInvitationServiceImpl.publishInvite(channelMember, inviteeId));
    }

    @Override
    public void inviteAcceptJoinAsMember(Member member, String code) {
        channelInvitationServiceImpl.inviteAcceptJoinAsMember(member, code);
    }

}
