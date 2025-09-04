package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long>, ChannelRepositoryCustom {
    boolean existsByTitle(String title);


}
