package com.zonbeozon.member.domain;

import com.zonbeozon.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String profile;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ServerRole role;
    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public Member(String username, String email, String profile, ServerRole role) {
        this.username = username;
        this.email = email;
        this.profile = profile;
        this.role = role;
        this.status = MemberStatus.ACTIVE;
    }

    public boolean isAdmin() {
        return this.role == ServerRole.ADMIN;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                '}';
    }
}
