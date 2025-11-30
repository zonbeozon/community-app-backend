package com.zonbeozon.chat.repository;

import com.zonbeozon.chat.domain.ChattingGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChattingGroupRepository extends JpaRepository<ChattingGroup, Long> {
    boolean existsByName(String name);
    Optional<ChattingGroup> findByName(String name);
}
