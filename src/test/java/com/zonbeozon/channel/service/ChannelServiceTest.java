package com.zonbeozon.channel.service;

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
    protected Member serverUser;
    protected Member serverAdmin;

    public ChannelServiceTest(MemberService memberService) {
        this.memberService = memberService;
    }

    @BeforeEach
    void setUp() {
        serverUser = memberService.createMemberWithRandomUsername("user1@gmail.com", "empty", ServerRole.USER);
        serverAdmin = memberService.createMemberWithRandomUsername("admin1@gmail.com", "empty", ServerRole.ADMIN);
    }
}
