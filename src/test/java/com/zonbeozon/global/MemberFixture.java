package com.zonbeozon.global;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;

public class MemberFixture {
    public static final Member member_user_1 = new Member(
            "naaab",
            "normal1@gmail.com",
            "https://fakeUrl.com",
            ServerRole.USER
    );
    public static final Member member_user_2 = new Member(
            "naaab",
            "normal2@gmail.com",
            "https://fakeUrl.com",
            ServerRole.USER
    );
    public static final Member member_user_3 = new Member(
            "ammy",
            "normal3@gmail.com",
            "https://fakeUrl.com",
            ServerRole.USER
    );
    public static final Member member_admin_1 = new Member(
            "allo",
            "admin1@gmail.com",
            "https://fakeUrl.com",
            ServerRole.USER
    );
    public static final Member member_admin_2 = new Member(
            "nick",
            "admin2@gmail.com",
            "https://fakeUrl.com",
            ServerRole.ADMIN
    );
}
