package com.zonbeozon.notification.repository;

import com.zonbeozon.notification.domain.Notification;
import com.zonbeozon.notification.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<NotificationDto> findByRecipientIdOrderByCreatedAtDesc(Long recipientId, Pageable pageable);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient.id = :recipientId AND n.isRead = false")
    int markAllAsReadByRecipientId(Long recipientId);
    long countByRecipientIdAndIsReadFalse(Long recipientId);
}
