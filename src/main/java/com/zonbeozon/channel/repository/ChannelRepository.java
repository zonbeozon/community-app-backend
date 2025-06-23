package com.zonbeozon.channel.repository;

import com.zonbeozon.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChannelRepository extends JpaRepository<Channel, Long>, ChannelRepositoryCustom {
    boolean existsByTitle(String title);

    @Modifying
    @Query("UPDATE Channel c SET c.isDeleted = true WHERE c.id = :id")
    void softDeleteById(Long id);
}
