package com.zonbeozon.channel.service;

import com.zonbeozon.channel.ChannelContext;
import com.zonbeozon.channel.ChannelPermissionValidator;
import com.zonbeozon.channel.UseChannelContext;
import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAccessDeniedException;
import com.zonbeozon.channel.exception.ChannelBadRequestException;
import com.zonbeozon.channel.exception.ChannelMemberNotFoundException;
import com.zonbeozon.channel.controller.ModifyChannelMemberRequest;
import com.zonbeozon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@UseChannelContext
public class ChannelMemberService {
    private final ChannelPermissionValidator permissionValidator;
    private final ChannelMemberRepository channelMemberRepository;

    @Transactional
    public void kickMember(ChannelContext channelContext, Long targetChannelMemberId) {
        ChannelMember memberToKick  = getByIdAndChannelOrThrow(targetChannelMemberId, channelContext.getChannel());
        permissionValidator.validate(ChannelAction.MEMBER_KICK, channelContext, memberToKick);
        memberToKick.updateStatus(ChannelMemberStatus.KICKED);
    }

    @Transactional
    public void joinAsMember(ChannelContext channelContext, Member requester) {
        channelContext.setMember(requester);
        permissionValidator.validate(ChannelAction.JOIN, channelContext);
        subscribeChannel(requester, channelContext.getChannel(), ChannelRole.CHANNEL_MEMBER);
    }

    @Transactional
    public void subscribeChannel(Member member, Channel channel, ChannelRole role) {
        Optional<ChannelMember> optChMember = channelMemberRepository.findByMemberAndChannel(member, channel);
        //기존에 존재했던 Member 일때
        if(optChMember.isPresent()) {
            ChannelMember chMember = optChMember.get();
            switch(chMember.getStatus()) {
                case ACTIVE -> throw new ChannelBadRequestException(member + "는 이미 " + channel + "에 속해있습니다.");
                case DELETED -> chMember.updateStatus(ChannelMemberStatus.ACTIVE);
                case KICKED -> throw new ChannelAccessDeniedException(member + "는 Kick된 상태입니다.");
            }
            return;
        }
        //신규 구독일때
        ChannelMember chMember = ChannelMember.create(member, channel, role);
        channelMemberRepository.save(chMember);
    }

    @Transactional
    public void modifyChannelMemberRole(ChannelContext channelContext, ModifyChannelMemberRequest request) {
        ChannelMember memberToModify = getByIdAndChannelOrThrow(request.targetChannelMemberId(), channelContext.getChannel());
        // 바꿀려는 Role과 현제 Role이 같을때
        if(memberToModify.getRole() == request.role()) {
            throw new ChannelBadRequestException("이미 해당 맴버는 해당 Role을 가지고 있습니다.");
        }
        //promote일때
        if (ChannelRole.isPromote(memberToModify.getRole(), request.role())) {
            permissionValidator.validate(ChannelAction.MEMBER_PROMOTION, channelContext, memberToModify);
            memberToModify.updateRole(request.role());
            return;
        }
        //demote일때
        permissionValidator.validate(ChannelAction.MEMBER_DEMOTION, channelContext, memberToModify);
        memberToModify.updateRole(request.role());
    }

    /**
     * @throws com.zonbeozon.channel.exception.ChannelAccessDeniedException
     * Owner가 unsubcribe시 발생 <br/>
     * Owner는 unsubscribe를 위해서 자신의 Owner Role을 다른 사람에게 이전해야 한다.
     */
    public void unsubscribeChannel(ChannelContext channelContext) {
        channelMemberRepository.delete(channelContext.getChannelMember());
    }

    public int getMemberCountByChannel(Channel channel) {
        return channelMemberRepository.countByChannel(channel);
    }

    @Transactional(readOnly = true)
    public ChannelMember getByMemberAndChannelOrThrow(Member member, Channel channel) {
        return channelMemberRepository.findByMemberAndChannel(member, channel)
                .orElseThrow(() -> new ChannelMemberNotFoundException(member + "는 채널: " + channel + "에 속해있지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<ChannelMember> getByMember(Member member) {
        return channelMemberRepository.findByMember(member);
    }


    @Transactional(readOnly = true)
    public ChannelMember getByIdAndChannelOrThrow(Long id, Channel channel) {
        return channelMemberRepository.findByIdAndChannel(id, channel)
                .orElseThrow(() -> new ChannelMemberNotFoundException(id + "를 가진 channelMember는 존재하지 않습니다."));
    }
}
