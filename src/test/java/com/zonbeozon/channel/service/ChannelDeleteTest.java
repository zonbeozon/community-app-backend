package com.zonbeozon.channel.service;

import com.zonbeozon.channel.repository.ChannelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChannelDeleteTest {
    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private ChannelRemover channelRemover;

//    @Test
//    @DisplayName("채널 Owner가 아니라면 채널삭제시 예외가 발생한다.")
//    void ownerCanDeleteChannelSuccessfully() {
//        channelRepository.findById(channel_1_id);
//        channelService.deleteChannel(channel_1_owner, channel_1_id);
//    }
//
//    @Test
//    @DisplayName("채널 Owner가 아니라면 예외를 발생시킨다.")
//    void deleteByNonOwnerThrowsAccessDenied() {
//        channelMemberService.joinAsMember(member_2, channel_1_id);
//
//        assertThatThrownBy(() -> channelService.deleteChannel(member_2, channel_1_id))
//                .isInstanceOf(ChannelAccessDeniedException.class)
//                .satisfies(e -> {
//                    ChannelAccessDeniedException exception = (ChannelAccessDeniedException) e;
//                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAccessDeniedException.ErrorCode.CHANNEL_DELETION_FORBIDDEN);
//                });
//    }
//
//    @Test
//    @DisplayName("채널에 가입한 유저가 아니라면 예외를 발생시킨다.")
//    void deleteByNonMemberThrowsMemberNotFound() {
//        assertThatThrownBy(() -> channelService.deleteChannel(member_2, channel_1_id))
//                .isInstanceOf(ChannelMemberNotFoundException.class);
//    }
}
