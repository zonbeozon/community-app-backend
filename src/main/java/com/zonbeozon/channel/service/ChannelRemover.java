package com.zonbeozon.channel.service;

import com.zonbeozon.channel.dto.ChannelDeletedEvent;
import com.zonbeozon.channel.entity.BlogChannel;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.global.exception.ErrorCode;
import com.zonbeozon.global.exception.NotFoundException;
import com.zonbeozon.post.service.PostRemover;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChannelRemover {
    private final ApplicationEventPublisher eventPublisher;
    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelProfileService channelProfileService;
    private final ChannelFinder channelFinder;
    private final PostRemover postRemover;

    public void removeChannel(Long channelId) {
        Channel channel = channelFinder.findByIdElseThrow(channelId);
        //채널 맴버 전부 삭제
        channelMemberRepository.deleteAllByChannelId(channelId);
        //채널 프로필 삭제
        channelProfileService.deleteProfile(channelId);
        //블로그 타입 채널이라면
        if(channel instanceof BlogChannel) {
            postRemover.deleteAllPostsByChannelId(channelId);
        }
        //채널 삭제
        channelRepository.deleteById(channelId);
        eventPublisher.publishEvent(new ChannelDeletedEvent(channelId));
    }
}
