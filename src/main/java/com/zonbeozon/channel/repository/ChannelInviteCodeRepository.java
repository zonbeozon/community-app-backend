package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import com.zonbeozon.channel.entity.ChannelInviteCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelInviteCodeRepository extends JpaRepository<ChannelInviteCode, Long> {
    Optional<ChannelInviteCode> findByCode(String code);
}
