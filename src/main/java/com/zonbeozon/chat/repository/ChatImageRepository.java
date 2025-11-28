package com.zonbeozon.chat.repository;

import com.zonbeozon.chat.domain.ChatImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ChatImageRepository extends JpaRepository<ChatImage, Long> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from ChatImage ci where ci in :chatImages")
    void deleteAll(Collection<ChatImage> chatImages);
}
