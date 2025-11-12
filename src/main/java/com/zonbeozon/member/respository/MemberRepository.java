package com.zonbeozon.member.respository;

import com.zonbeozon.member.domain.Member;
import com.zonbeozon.member.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.profile p LEFT JOIN FETCH p.image WHERE m.id = :id")
    Optional<Member> findByIdWithProfile(Long id);

}
