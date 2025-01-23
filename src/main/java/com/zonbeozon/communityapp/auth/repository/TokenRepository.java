package com.zonbeozon.communityapp.auth.repository;

import com.zonbeozon.communityapp.auth.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken);
    void deleteByMemberKey(String memberKey);
}
