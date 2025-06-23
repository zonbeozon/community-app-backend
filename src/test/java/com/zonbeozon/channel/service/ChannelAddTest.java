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
    public ChannelAddTest(MemberService memberService) {
        super(memberService);
    }

    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelMemberRepository channelMemberRepository;
    @Autowired
    private ChannelRepository channelRepository;

    ChannelCreateCommand command = new ChannelCreateCommand(
            "title",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.DENY,
            ChannelSearchLevel.PUBLIC
    );

    @Test
    @DisplayName("command로 부터 정상적으로 채널이 생성되어야 한다.")
    void createsChannelSuccessfullyFromCommand() {
       Long id = channelService.addChannel(command, serverUser);
       Channel channel = channelRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 Id와 맞는 채널이 없습니다"));

       ChannelMember channelMember = channelMemberRepository.findByMemberAndChannel(serverUser, channel)
               .orElseThrow(() -> new RuntimeException("해당 조건에 해당하는 채널 맴버가 없습니다."));

       assertThat(channel.getTitle()).isEqualTo(command.title());
       assertThat(channel.getDescription()).isEqualTo(command.description());
       assertThat(channel.getProfile()).isEqualTo(command.profile());
       assertThat(channel.getContentOpenLevel()).isEqualTo(command.contentOpenLevel());
       assertThat(channel.getType()).isEqualTo(command.type());
       assertThat(channel.getJoinLevel()).isEqualTo(command.joinLevel());
       assertThat(channel.getSearchLevel()).isEqualTo(command.searchLevel());
       assertThat(channelMember.getMember()).isEqualTo(serverUser);
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

        assertThatThrownBy(()-> channelService.addChannel(officialInfoChannelCreateCommand, serverUser))
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
        channelService.addChannel(command, serverUser);

        //second time create with same title
        assertThatThrownBy(()-> channelService.addChannel(command, serverUser))
                .isInstanceOf(ChannelAddException.class)
                .satisfies(e -> {
                    ChannelAddException exception = (ChannelAddException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAddException.ErrorCode.DUPLICATE_CHANNEL_TITLE);
                });
    }

    @Test
    @DisplayName("검색 가능 여부가 Private이지만 열람 설정이 Public이라면 예외를 발생시킨다.")
    void throwExceptionWhenSearchIsPrivateAndContentIsPublic() {
        ChannelCreateCommand command = new ChannelCreateCommand(
                "title",
                "description",
                "emtpyProfile",
                ChannelContentOpenLevel.PUBLIC,
                ChannelType.COMMUNITY_INFO,
                ChannelJoinLevel.DENY,
                ChannelSearchLevel.PRIVATE
        );

        assertThatThrownBy(()-> channelService.addChannel(command, serverUser))
                .isInstanceOf(ChannelAddException.class)
                .satisfies(e -> {
                    ChannelAddException exception = (ChannelAddException) e;
                    assertThat(exception.getErrorCode()).isEqualTo(ChannelAddException.ErrorCode.OPEN_LEVEL_MISMATCH);
                });
    }
}
