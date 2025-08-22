package com.zonbeozon.auth.repository;

import com.zonbeozon.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Token t WHERE t.member.id = :memberId")
    void deleteTokenByMemberId(Long memberId);
}
