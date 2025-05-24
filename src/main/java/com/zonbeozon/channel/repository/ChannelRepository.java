package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long>, ChannelRepositoryCustom {
    boolean existsByTitle(String title);
}
