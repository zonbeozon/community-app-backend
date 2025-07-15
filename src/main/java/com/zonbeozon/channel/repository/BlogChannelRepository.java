package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.BlogChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogChannelRepository extends JpaRepository<BlogChannel, Long>, BlogChannelRepositoryCustom {
}
