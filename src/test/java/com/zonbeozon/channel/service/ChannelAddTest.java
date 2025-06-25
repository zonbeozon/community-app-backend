package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.*;
import com.zonbeozon.channel.exception.ChannelAddException;
import com.zonbeozon.channel.repository.ChannelMemberRepository;
import com.zonbeozon.channel.repository.ChannelRepository;
import com.zonbeozon.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class ChannelAddTest extends ChannelServiceTest {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    public ChannelAddTest(MemberService memberService) {
        super(memberService);
    }

    @Test
    @DisplayName("command로 부터 정상적으로 채널이 생성되어야 한다.")
    void createsChannelSuccessfullyFromCommand() {
       Long id = channelService.addChannel(validChannelCreateCommand_1, serverUser_1);
       Channel channel = channelRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 Id와 맞는 채널이 없습니다"));

       ChannelMember channelMember = channelMemberRepository.findByMemberAndChannel(serverUser_1, channel)
               .orElseThrow(() -> new RuntimeException("해당 조건에 해당하는 채널 맴버가 없습니다."));

       assertThat(channel.getTitle()).isEqualTo(validChannelCreateCommand_1.title());
       assertThat(channel.getDescription()).isEqualTo(validChannelCreateCommand_1.description());
       assertThat(channel.getProfile()).isEqualTo(validChannelCreateCommand_1.profile());
       assertThat(channel.getContentOpenLevel()).isEqualTo(validChannelCreateCommand_1.contentOpenLevel());
       assertThat(channel.getType()).isEqualTo(validChannelCreateCommand_1.type());
       assertThat(channel.getJoinLevel()).isEqualTo(validChannelCreateCommand_1.joinLevel());
       assertThat(channel.getSearchLevel()).isEqualTo(validChannelCreateCommand_1.searchLevel());
       assertThat(channelMember.getMember()).isEqualTo(serverUser_1);
    }

    @Test
    @DisplayName("USER_ROLE 유저가 Community채널이 아닌 채널을 만든다면 예외를 발생시킨다")
    void throwsAccessDeniedWhenUserRoleCreatesNonCommunityChannel() {
        ChannelCreateCommand officialInfoChannelCreateCommand = new ChannelCreateCommand(
                "title",
                "description",
                "emtpyProfile",
                ChannelContentOpenLevel.PUBLIC,
                ChannelType.OFFICIAL_INFO,
                ChannelJoinLevel.DENY,
                ChannelSearchLevel.PUBLIC
        );

        assertThatThrownBy(()-> channelService.addChannel(officialInfoChannelCreateCommand, serverUser_1))
                .isInstanceOf(ChannelAddException.class)
                .satisfies(e -> {
                    ChannelAddException exception = (ChannelAddException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAddException.ErrorCode.ACCESS_DENIED);
                });
    }

    @Test
    @DisplayName("중복 채널명은 예외를 발생시킨다.")
    void throwsExceptionWhenCreatingChannelWithDuplicateTitle() {
        //first time create
        channelService.addChannel(validChannelCreateCommand_1, serverUser_1);

        //second time create with same title
        assertThatThrownBy(()-> channelService.addChannel(validChannelCreateCommand_1, serverUser_1))
                .isInstanceOf(ChannelAddException.class)
                .satisfies(e -> {
                    ChannelAddException exception = (ChannelAddException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAddException.ErrorCode.DUPLICATE_CHANNEL_TITLE);
                });
    }

    @Test
    @DisplayName("검색 가능 여부가 Private이지만 열람 설정이 Public이라면 예외를 발생시킨다.")
    void throwExceptionWhenSearchIsPrivateAndContentIsPublic() {
        ChannelCreateCommand invalidCommand = new ChannelCreateCommand(
                "title",
                "description",
                "emtpyProfile",
                ChannelContentOpenLevel.PUBLIC,
                ChannelType.COMMUNITY_INFO,
                ChannelJoinLevel.DENY,
                ChannelSearchLevel.PRIVATE
        );

        assertThatThrownBy(()-> channelService.addChannel(invalidCommand, serverUser_1))
                .isInstanceOf(ChannelAddException.class)
                .satisfies(e -> {
                    ChannelAddException exception = (ChannelAddException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAddException.ErrorCode.INVALID_CHANNEL_SETTING_COMBINATION);
                });
    }
}
