package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.InfoChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoChannelRepository extends JpaRepository<InfoChannel, Long>, InfoChannelRepositoryCustom {
}
