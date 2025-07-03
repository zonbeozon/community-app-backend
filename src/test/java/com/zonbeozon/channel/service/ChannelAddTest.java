package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAccessDeniedException;
import com.zonbeozon.channel.exception.ChannelBadRequestException;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.member.BaseMemberTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.zonbeozon.channel.service.ChannelFixture.*;
import static org.assertj.core.api.Assertions.*;

public class ChannelAddTest extends BaseMemberTest {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("command로 부터 정상적으로 채널이 생성되어야 한다.")
    void createsChannelSuccessfullyFromCommand() {
       Long id = channelService.addChannel(CHANNEL_ADD_COMMAND_1, member_1);
       Channel channel = channelRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 Id와 맞는 채널이 없습니다"));

       ChannelMember channelMember = channelMemberRepository.findByMemberAndChannel(member_1, channel)
               .orElseThrow(() -> new RuntimeException("해당 조건에 해당하는 채널 맴버가 없습니다."));

       assertChannelMetadataEquals(channel, CHANNEL_ADD_COMMAND_1);
       assertThat(channelMember.getMember()).isEqualTo(member_1);
    }

    @Test
    @DisplayName("USER_ROLE 유저가 Community채널이 아닌 채널을 만든다면 예외를 발생시킨다")
    void throwsAccessDeniedWhenUserRoleCreatesNonCommunityChannel() {
        assertThatThrownBy(()-> channelService.addChannel(CHANNEL_ADD_COMMAND_3, member_1))
                .isInstanceOf(ChannelAccessDeniedException.class)
                .satisfies(e -> {
                    ChannelAccessDeniedException exception = (ChannelAccessDeniedException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAccessDeniedException.ErrorCode.CHANNEL_CREATION_FORBIDDEN);
                });
    }

    @Test
    @DisplayName("중복 채널명은 예외를 발생시킨다.")
    void throwsExceptionWhenCreatingChannelWithDuplicateTitle() {
        //first time create
        channelService.addChannel(CHANNEL_ADD_COMMAND_1, member_1);

        //second time create with same title
        assertThatThrownBy(()-> channelService.addChannel(CHANNEL_ADD_COMMAND_1, member_1))
                .isInstanceOf(ChannelBadRequestException.class)
                .satisfies(e -> {
                    ChannelBadRequestException exception = (ChannelBadRequestException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelBadRequestException.ErrorCode.DUPLICATE_CHANNEL_TITLE);
                });
    }

    @Test
    @DisplayName("검색 가능 여부가 Private이지만 열람 설정이 Public이라면 예외를 발생시킨다.")
    void throwExceptionWhenSearchIsPrivateAndContentIsPublic() {
        ChannelAddCommand invalidCommand = new ChannelAddCommand(
                "title",
                "description",
                "emtpyProfile",
                ChannelContentOpenLevel.PUBLIC,
                ChannelType.COMMUNITY_INFO,
                ChannelJoinLevel.DENY,
                ChannelSearchLevel.PRIVATE
        );

        assertThatThrownBy(()-> channelService.addChannel(invalidCommand, member_1))
                .isInstanceOf(ChannelBadRequestException.class)
                .satisfies(e -> {
                    ChannelBadRequestException exception = (ChannelBadRequestException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelBadRequestException.ErrorCode.INVALID_CHANNEL_SETTING_COMBINATION);
                });
    }

    static void assertChannelMetadataEquals(Channel channel, ChannelAddCommand command) {
        assertThat(channel.getTitle()).isEqualTo(command.title());
        assertThat(channel.getDescription()).isEqualTo(command.description());
        assertThat(channel.getProfile()).isEqualTo(command.profile());
        assertThat(channel.getContentOpenLevel()).isEqualTo(command.contentOpenLevel());
        assertThat(channel.getJoinLevel()).isEqualTo(command.joinLevel());
        assertThat(channel.getSearchLevel()).isEqualTo(command.searchLevel());
        assertThat(channel.getType()).isEqualTo(command.type());
    }
}
