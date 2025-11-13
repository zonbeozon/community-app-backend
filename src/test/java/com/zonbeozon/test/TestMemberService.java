package com.zonbeozon.test;

import com.zonbeozon.auth.dto.SimpleAuthenticatedPrincipal;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.MemberProfile;
import com.zonbeozon.member.domain.ServerRole;
import com.zonbeozon.member.respository.MemberProfileRepository;
import com.zonbeozon.member.respository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TestMemberService {
    @Autowired
    private MemberRepository memberRepository;

    private static final String DEFAULT_NAME = "test-user";
    @Autowired
    private MemberProfileRepository memberProfileRepository;

    public Member createAndSave(String name) {
        Member member = new Member(name, name + "@gmail.com", ServerRole.USER);
        memberRepository.save(member);
        return member;
    }

    public Member createAndSave() {
        return createAndSave(DEFAULT_NAME);
    }

    public Member setProfile(Member member, MemberProfile profile) {
        member.setProfile(profile);
        return member;
    }

    public Member setServerRole(Member member, ServerRole serverRole) {
        member.updateServerRole(serverRole);
        return member;
    }

    public List<Member> createAndSaveMembers(String prefix, int numberOfMembers) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < numberOfMembers; i++) {
            Member member = createAndSave(prefix + i);
            memberRepository.save(member);
            members.add(member);
        }
        return members;
    }

    public void setSecurityContext(Member member) {
        AuthenticatedPrincipal authenticatedPrincipal = new SimpleAuthenticatedPrincipal(member.getId().toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedPrincipal,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getKey()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }
}
