package com.zonbeozon.member;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class BaseMemberTest {
    @Autowired
    private MemberService memberService;

    protected Member member_1;
    protected Member member_2;
    protected Member member_3;

    protected Member admin_1;

    @BeforeEach
    void setup() {
        System.out.println("called");
        member_1 = memberService.createMember("member_1", "member_1@gmail.com", "profileOfMember_1", ServerRole.USER);
        member_2 = memberService.createMember("member_2", "member_2@gmail.com", "profileOfMember_2", ServerRole.USER);
        member_3 = memberService.createMember("member_3", "member_3@gmail.com", "profileOfMember_3", ServerRole.USER);

        admin_1 = memberService.createMember("admin_1", "admin_1@gmail.com", "profileOfAdmin_1", ServerRole.ADMIN);
    }
}
