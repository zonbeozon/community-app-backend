package com.zonbeozon.communityapp.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    private String memberKey;
    private String refreshToken;
    @Setter
    private String accessToken;

    public Token(String memberKey, String accessToken, String refreshToken) {
        this.memberKey = memberKey;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

}
