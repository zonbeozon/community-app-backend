package com.zonbeozon.channel;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.exception.ChannelMemberNotFoundException;
import com.zonbeozon.channel.exception.ChannelNotFoundException;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

@Aspect
@Component
@RequiredArgsConstructor
public class ChannelContextAspect {
    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;

    @Around("@within(com.zonbeozon.channel.UseChannelContext)")
    public Object resolveChannelContext(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Parameter[] params = signature.getMethod().getParameters();

        for (int i = 0; i < params.length; i++) {
            if (params[i].getType().equals(ChannelContext.class) && args[i] == null) {
                ChannelContext channelContext = (ChannelContext) args[i];
                Channel channel = channelRepository.findById(channelContext.getChannelId())
                        .orElseThrow(() -> new ChannelNotFoundException(channelContext.getChannelId() + "은 존재하지 않는 channelId입니다."));
                channelContext.setChannel(channel);
                if(channelContext.getMember() == null) {
                    return pjp.proceed();
                }
                ChannelMember chMember = channelMemberRepository.findByMemberAndChannel(channelContext.getMember(), channel)
                        .orElseThrow(() -> new ChannelMemberNotFoundException(channelContext.getMember() + "는 채널: " + channel + "에 속해있지 않습니다."));
                channelContext.setChannelMember(chMember);
            }
        }
        return pjp.proceed(args);
    }
}
