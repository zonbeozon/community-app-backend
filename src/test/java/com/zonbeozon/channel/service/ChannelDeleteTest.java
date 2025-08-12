package com.zonbeozon.channel.service;

import com.zonbeozon.channel.TestChannelBuilder;
import com.zonbeozon.channel.TestChannelMemberBuilder;
import com.zonbeozon.channel.dto.ChannelDeletedEvent;
import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.enums.ChannelRole;

import com.zonbeozon.global.exception.AccessDeniedException;
import com.zonbeozon.member.TestMemberBuilder;
import com.zonbeozon.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@RecordApplicationEvents
public class ChannelDeleteTest {
    @Autowired
    private ChannelRemover channelRemover;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ApplicationEvents applicationEvents;

    private Member member_1, member_2;
    private Channel channel;

    @BeforeEach
    void setUp() {
        member_1 = new TestMemberBuilder("choi", "choi@gmail.com").persistAndSetSecurityContext(entityManager);
        member_2 = new TestMemberBuilder("yunghi", "yunghi@gmail.com").persist(entityManager);
        channel = new TestChannelBuilder().persist(entityManager);
    }

    @Test
    @DisplayName("채널 Owner가 아니라면 채널삭제시 예외가 발생한다.")
    void ownerCanDeleteChannelSuccessfully() {
        new TestChannelMemberBuilder(member_1, channel).withRole(ChannelRole.CHANNEL_ADMIN).persist(entityManager);
        Assertions.assertThatThrownBy(()->channelRemover.removeChannel(channel.getId()))
                .isInstanceOf(AccessDeniedException.class);
    }


    @Test
    @DisplayName("채널 삭제 이벤트를 발생시킨다.")
    void publishChannelDeletedEventOnChannelRemoval() {
        new TestChannelMemberBuilder(member_1, channel).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);
        channelRemover.removeChannel(channel.getId());
        List<ChannelDeletedEvent> events = applicationEvents.stream(ChannelDeletedEvent.class).toList();
        assertThat(events).hasSize(1);
        assertThat(events.get(0).channelId()).isEqualTo(channel.getId());
    }

    @Test
    @DisplayName("채널 삭제시 채널 상태는 DELETE로 변경 되어야한다.")
    void ChangeStatusToDeletedWhenChannelIsRemoved() {
        new TestChannelMemberBuilder(member_1, channel).withRole(ChannelRole.CHANNEL_OWNER).persist(entityManager);
        channelRemover.removeChannel(channel.getId());
        Assertions.assertThat(channel.isDeleted()).isTrue();
    }
}
