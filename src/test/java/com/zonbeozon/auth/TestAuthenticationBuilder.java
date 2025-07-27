package com.zonbeozon.auth;

import com.zonbeozon.member.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class TestAuthenticationBuilder {
    private Long memberId;
    private String role;

    public TestAuthenticationBuilder(Member member) {
        this.memberId = member.getId();
        role = member.getRole().getKey();
    }

    public TestAuthenticationBuilder withMemberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public TestAuthenticationBuilder withRole(String role) {
        this.role = role;
        return this;
    }


    public Authentication build() {
        return new Authentication() {

            @Override
            public String getName() {
                return memberId.toString();
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(role));
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }
        };
    }
}
