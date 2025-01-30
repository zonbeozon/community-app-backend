package com.zonbeozon.communityapp.auth.repository;

import com.zonbeozon.communityapp.auth.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken);
    void deleteByMemberKey(String memberKey);
    @Modifying
    @Query("DELETE FROM Token t"
    + " WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") Date now);
}
