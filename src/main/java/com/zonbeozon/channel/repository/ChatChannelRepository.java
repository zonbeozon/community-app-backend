package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.ChatChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, Long> {
}
