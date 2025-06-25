package com.zonbeozon.channel.service;

import com.zonbeozon.channel.entity.ChannelContentOpenLevel;
import com.zonbeozon.channel.entity.ChannelJoinLevel;
import com.zonbeozon.channel.entity.ChannelSearchLevel;
import com.zonbeozon.channel.entity.ChannelType;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
abstract class ChannelServiceTest {
    private final MemberService memberService;
    protected Member serverUser_1;
    protected Member serverUser_2;

    protected ChannelCreateCommand validChannelCreateCommand_1 = new ChannelCreateCommand(
            "title1",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );
    protected ChannelCreateCommand validChannelCreateCommand_2 = new ChannelCreateCommand(
            "title2",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );

    protected ChannelCreateCommand validChannelCreateCommand_3 = new ChannelCreateCommand(
            "title3",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );

    protected ChannelCreateCommand validChannelCreateCommand_4 = new ChannelCreateCommand(
            "title4",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );

    protected ChannelCreateCommand validChannelCreateCommand_5 = new ChannelCreateCommand(
            "title5",
            "description",
            "emtpyProfile",
            ChannelContentOpenLevel.PUBLIC,
            ChannelType.COMMUNITY_INFO,
            ChannelJoinLevel.OPEN,
            ChannelSearchLevel.PUBLIC
    );



    public ChannelServiceTest(MemberService memberService) {
        this.memberService = memberService;
    }

    @BeforeEach
    void setUp() {
        serverUser_1 = memberService.createMemberWithRandomUsername("user1@gmail.com", "empty", ServerRole.USER);
        serverUser_2 = memberService.createMemberWithRandomUsername("admin1@gmail.com", "empty", ServerRole.USER);
    }
}
