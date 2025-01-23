package com.zonbeozon.communityapp.member.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String profile;
    @Column(nullable = false, unique = true)
    private String memberKey;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String username, String email, String profile, String memberKey, Role role) {
        this.username = username;
        this.email = email;
        this.profile = profile;
        this.memberKey = memberKey;
        this.role = role;
    }
}
