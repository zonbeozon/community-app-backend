package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.JoinRequestApprovedEvent;
import com.zonbeozon.channel.dto.JoinRequestDeniedEvent;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.entity.PendingChannelMember;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelRole;
import com.zonbeozon.channel.enums.JoinResultStatus;
import com.zonbeozon.channel.repository.BannedChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.PendingChannelMemberRepository;
import com.zonbeozon.channel.service.finder.ChannelFinder;
import com.zonbeozon.channel.service.finder.ChannelMemberFinder;
import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.global.exception.ConflictException;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.service.MemberFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelMemberJoiner {
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelFinder channelFinder;
    private final ChannelMemberFinder channelMemberFinder;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberFinder memberFinder;
    private final BannedChannelMemberRepository bannedChannelMemberRepository;
    private final PendingChannelMemberRepository pendingChannelMemberRepository;

    public void joinAsOwner(Long channelId, Long requesterId) {
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        Member requester = memberFinder.findByIdElseThrow(requesterId);
        if(channelMemberRepository.existsByChannelAndRole(channel, ChannelRole.CHANNEL_OWNER))
            throw new IllegalStateException("중복 owner가 발생했습니다.");
        join(channel, requester, ChannelRole.CHANNEL_OWNER);
    }

    public JoinResultStatus joinAsMember(Long channelId, Long requesterId) {
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        Member requester = memberFinder.findByIdElseThrow(requesterId);
        if(channel.getSetting().getJoinPolicy() == ChannelJoinPolicy.DENY) {
            throw new AccessDeniedException(ErrorCode.CHANNEL_JOIN_DENIED);
        }

        //ban된 맴버는 참여 불가.
        if(bannedChannelMemberRepository.existsByChannelIdAndMemberId(channelId, requesterId))
            throw new AccessDeniedException(ErrorCode.BANNED_MEMBER_CANNOT_JOIN);

        //채널 가입 정책이 승인이라면
        if(channel.getSetting().getJoinPolicy() == ChannelJoinPolicy.APPROVAL) {
            joinAsPending(requester, channel);
            return JoinResultStatus.APPROVAL_REQUESTED;
        }
        //공개 가입 채널이라면
        join(channel, requester, ChannelRole.CHANNEL_MEMBER);
        return JoinResultStatus.JOINED_IMMEDIATELY;
    }

    public void approveJoinRequest(Long channelId, Long requesterId) {
        channelFinder.findByIdElseThrow(channelId);
        PendingChannelMember pendingChannelMember = pendingChannelMemberRepository.findByChannelIdAndMemberIdWithChannelAndMember(channelId, requesterId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_IS_NOT_PENDING_STATUS));
        join(pendingChannelMember.getChannel(), pendingChannelMember.getMember(), ChannelRole.CHANNEL_MEMBER);
        pendingChannelMemberRepository.delete(pendingChannelMember);
        eventPublisher.publishEvent(new JoinRequestApprovedEvent(channelId, requesterId));
    }

    public void denyJoinRequest(Long channelId, Long requesterId) {
        channelFinder.findByIdElseThrow(channelId);
        PendingChannelMember pendingChannelMember = pendingChannelMemberRepository.findByChannelIdAndMemberId(channelId, requesterId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_MEMBER_IS_NOT_PENDING_STATUS));
        pendingChannelMemberRepository.delete(pendingChannelMember);
        eventPublisher.publishEvent(new JoinRequestDeniedEvent(channelId, requesterId));
    }

    private void join(Channel channel, Member requester, ChannelRole role) {
        if(channelMemberFinder.existsByChannelIdAndMemberId(channel.getId(), requester.getId()))
            throw new ConflictException(ErrorCode.ALREADY_JOINED_CHANNEL);
        ChannelMember channelMember = ChannelMember.create(requester, channel, role);
        channelMemberRepository.save(channelMember);
    }

    private void joinAsPending(Member requester, Channel channel) {
        if(pendingChannelMemberRepository.existsByChannelIdAndMemberId(channel.getId(), requester.getId())) {
            throw new ConflictException(ErrorCode.ALREADY_JOINED_CHANNEL);
        }
        pendingChannelMemberRepository.save(new PendingChannelMember(requester, channel));
    }
}
