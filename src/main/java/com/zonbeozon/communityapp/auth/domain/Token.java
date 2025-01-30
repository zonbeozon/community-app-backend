package com.zonbeozon.communityapp.auth.domain;

import com.zonbeozon.communityapp.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(nullable = false)
    private String memberKey;
    @Column(nullable = false)
    private String refreshToken;
    @Setter
    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private Date expiresAt;

    public Token(String memberKey, String accessToken, String refreshToken, Date expiresAt) {
        this.memberKey = memberKey;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public Token updateRefreshToken(String refreshToken, Date expiresAt) {
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        return this;
    }

}
