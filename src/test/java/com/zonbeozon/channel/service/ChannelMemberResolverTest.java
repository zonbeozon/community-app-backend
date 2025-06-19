package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelMember;
import com.zonbeozon.channel.exception.ChannelMemberNotFoundException;
import com.zonbeozon.member.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ChannelMemberResolverTest {

    @Mock
    private ChannelEntityQueryService channelEntityQueryService;

    @Mock
    private ChannelMemberEntityQueryService channelMemberEntityQueryService;

    @Mock
    private Member mockMember;

    @Mock
    private Channel mockChannel;

    @Mock
    private ChannelMember mockChannelMember;

    @InjectMocks
    private ChannelMemberResolver channelMemberResolver;

    private final Long channelId = 1L;

    @Test
    @DisplayName("채널id에 맞는 채널이 존재하고 맴버가 채널에 가입되어 있다면 null이 아닌 channelMember를 받는다.")
    void d() {
        // given
        Mockito.when(channelEntityQueryService.getChannelByIdOrThrow(channelId)).thenReturn(mockChannel);
        Mockito.when(channelMemberEntityQueryService.getChannelMemberOrThrow(mockMember, mockChannel)).thenReturn(mockChannelMember);

        // when & then
        channelMemberResolver.findChannelMemberThenConsume(mockMember, channelId, channelMember ->
                Assertions.assertThat(channelMember).isNotNull()
        );
    }

    @Test
    @DisplayName("채널id에 맞는 채널이 존재하고 맴버가 채널에 가입되어 있지 않다면 nullable이 false라면 예외가 발생된다.")
    void throwsException_whenMemberNotInChannelAndNullableFalse() {
        // given
        Mockito.when(channelEntityQueryService.getChannelByIdOrThrow(channelId)).thenReturn(mockChannel);
        Mockito.when(channelMemberEntityQueryService.getChannelMemberOrThrow(mockMember, mockChannel))
                .thenThrow(new ChannelMemberNotFoundException(""));

        // when & then
        Assertions.assertThatThrownBy(() ->
                channelMemberResolver.findChannelMemberThenConsume(mockMember, channelId, cm -> {}, false)
        ).isInstanceOf(ChannelMemberNotFoundException.class);
    }

    @Test
    @DisplayName("채널id에 맞는 채널이 존재하고 맴버가 채널에 가입되어 있지 않다면 nullable이 true라면 null을 받는다.")
    void returnsNull_whenMemberNotInChannelAndNullableTrue() {
        // given
        Mockito.when(channelEntityQueryService.getChannelByIdOrThrow(channelId)).thenReturn(mockChannel);
        Mockito.when(channelMemberEntityQueryService.getChannelMember(mockMember, mockChannel)).thenReturn(Optional.empty());

        // when & then
        channelMemberResolver.findChannelMemberThenConsume(mockMember, channelId, channelMember ->
                        Assertions.assertThat(channelMember).isNull(),true);
    }
}
