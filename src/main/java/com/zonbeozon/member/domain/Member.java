package com.zonbeozon.member.domain;

import com.zonbeozon.global.entity.BaseTimeEntity;
import com.zonbeozon.image.entity.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
public class Member extends BaseTimeEntity {
    public static final String ALLOWED_USERNAME_PATTERN = "^[가-힣a-zA-Z0-9_]{2,32}$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "member")
    private MemberProfile profile;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ServerRole role;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public Member(String username, String email, ServerRole role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = MemberStatus.ACTIVE;
    }

    public boolean isAdmin() {
        return this.role == ServerRole.ADMIN;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void setProfile(MemberProfile profile) {
        this.profile = profile;
    }

    public Image getImage() {
        return (this.profile == null) ? null : this.profile.getImage();
    }

    public void updateServerRole(ServerRole role) {
        this.role = role;
    }

    public void deleteMember() {
        this.status = MemberStatus.DELETED;
    }
}
