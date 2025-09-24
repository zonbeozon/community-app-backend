package com.zonbeozon.integral.stomp;

import com.zonbeozon.auth.TestAuthenticationBuilder;
import com.zonbeozon.base.AbstractChannelIntegrationTest;
import com.zonbeozon.channel.ChannelSubscriptionValidator;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.global.exception.stomp.SubscriptionException;
import com.zonbeozon.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public class ChannelSubscribeTest extends AbstractChannelIntegrationTest {
    @Autowired
    private ChannelSubscriptionValidator channelSubscriptionValidator;

    private Channel channel;
    private Member member;
    private StompHeaderAccessor subscribeAccessor;

    @BeforeEach
    void setup() {
        channel = testBlogChannelService.createAndSave();
        member = testMemberService.createAndSave();
        subscribeAccessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        subscribeAccessor.setDestination("/topic/channel/" + channel.getId());
        subscribeAccessor.setUser(new TestAuthenticationBuilder(member).build());
    }

    @DisplayName("채널에 참가된 유저면 구독가능 하다.")
    @Test
    void subscribeWhenUserIsChannelMember()  {
        testBlogChannelService.joinAsMember(channel, member);
        channelSubscriptionValidator.handle(subscribeAccessor);
    }

    @DisplayName("채널에 참가하지 않은 유저라면 예외가 발생한다.")
    @Test
    void throwExceptionWhenUserIsNotChannelMember() {
        Assertions.assertThatThrownBy(() -> channelSubscriptionValidator.handle(subscribeAccessor)
        ).isInstanceOf(SubscriptionException.class);
    }
}
