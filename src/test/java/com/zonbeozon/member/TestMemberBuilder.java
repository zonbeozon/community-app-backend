package com.zonbeozon.member;

import com.zonbeozon.SecurityTestUtils;
import com.zonbeozon.auth.dto.SimpleAuthenticatedPrincipal;
import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.domain.ServerRole;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

public class TestMemberBuilder {
    private String username = "choi";
    private String email = "choi@gmail.com";
    private String profile = "exampleProfile";
    private ServerRole role = ServerRole.USER;

    public TestMemberBuilder(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public TestMemberBuilder() {
    }

    public TestMemberBuilder setRole(ServerRole role) {
        this.role = role;
        return this;
    }

    public Member build() {
        return new Member(username, email, profile, role);
    }

    public Member persist(EntityManager entityManager) {
        Member member = build();
        entityManager.persist(member);
        return member;
    }

    public Member persistAndSetSecurityContext(EntityManager entityManager) {
        Member member = persist(entityManager);
        AuthenticatedPrincipal authenticatedPrincipal = new SimpleAuthenticatedPrincipal(member.getId().toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedPrincipal,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole().getKey()))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return member;
    }


}
