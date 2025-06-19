package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAccessDeniedException;
import com.zonbeozon.channel.exception.ChannelBadRequestException;
import com.zonbeozon.channel.exception.ChannelMemberNotFoundException;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class ChannelMemberServiceImpl implements ChannelMemberEntityQueryService {
    private final ChannelMemberRepository channelMemberRepository;

    @Transactional
    public void joinAsOwner(Member member, Channel channel) {
        if(channelMemberRepository.existsByChannelAndRole(channel, ChannelRole.CHANNEL_OWNER))
            throw new ChannelBadRequestException("Owner는 채널당 한명만 존재 가능합니다");
        join(member, channel, ChannelRole.CHANNEL_OWNER);
    }

    @Transactional
    public void joinAsMember(Member member, Channel channel) {
        channel.validateJoinPermission();
        if(channelMemberRepository.isKicked(member, channel)) {
            throw new ChannelAccessDeniedException("이전에 Kick당한 맴버는 초대를 통해서만 재가입할 수 있습니다.");
        }
        join(member, channel, ChannelRole.CHANNEL_MEMBER);
    }

    @Transactional
    public void inviteAcceptJoinAsMember(Member member, Channel channel) {
        join(member, channel, ChannelRole.CHANNEL_MEMBER);
    }

    private void join(Member member, Channel channel, ChannelRole role) {
        if(channelMemberRepository.existsByMemberAndChannel(member, channel))
            throw new ChannelBadRequestException("이미 해당 유저는 채널에 가입했습니다");
        ChannelMember chMember = ChannelMember.create(member, channel, role);
        channelMemberRepository.save(chMember);
    }

    @Transactional
    public void kickMember(ChannelMember actor, ChannelMember target) {
        if(!actor.getChannel().equals(target.getChannel()))
            throw new ChannelBadRequestException("타겟맴버가 다른 채널의 맴버입니다");
        actor.getChannel().kick(actor, target);
    }

    @Transactional
    public void modifyChannelMemberRole(ChannelMember actor, ChannelMember target, ChannelRole wantToChange) {
        if(!actor.getChannel().equals(target.getChannel()))
            throw new ChannelBadRequestException("타겟맴버가 다른 채널의 맴버입니다");
        actor.getChannel().modifyRole(actor, target, wantToChange);
    }

    /**
     * @throws com.zonbeozon.channel.exception.ChannelBadRequestException
     * Owner가 호출시 발생 <br/>
     * Owner는 나가기 위해서 자신의 Owner Role을 다른 사람에게 이전해야 한다.
     */
    @Transactional
    public void leaveChannel(ChannelMember actor) {
        if(actor.canLeaveChannel()) {
            throw new ChannelBadRequestException("채널을 탈퇴할 수 없습니다");
        }
        channelMemberRepository.delete(actor);
    }

    @Transactional(readOnly = true)
    public int getMemberCountByChannel(Channel channel) {
        return channelMemberRepository.countByChannel(channel);
    }

    @Transactional(readOnly = true)
    public Optional<ChannelMember> getChannelMember(Member member, Channel channel) {
        return channelMemberRepository.findByMemberAndChannel(member, channel);
    }

    @Transactional(readOnly = true)
    public ChannelMember getChannelMemberOrThrow(Member member, Channel channel) {
        return getChannelMember(member, channel)
                .orElseThrow(() -> new ChannelMemberNotFoundException(member + "는 채널: " + channel + "에 속해있지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<ChannelMember> getChannelMembersByMember(Member member) {
        return channelMemberRepository.findByMember(member);
    }

    @Transactional(readOnly = true)
    public ChannelMember getByIdOrThrow(Long id) {
        return channelMemberRepository.findById(id)
                .orElseThrow(() -> new ChannelMemberNotFoundException(id + "를 가진 channelMember는 존재하지 않습니다."));
    }
}
