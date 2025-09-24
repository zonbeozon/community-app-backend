package com.zonbeozon.notification.domain;

import com.zonbeozon.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(indexes = {
        @Index(name = "idx_notification_recipient_createdAt", columnList = "recipient_id, createdAt DESC"),
        @Index(name = "idx_notification_recipient_read", columnList = "recipient_id, isRead")
})
@EntityListeners(AuditingEntityListener.class)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isRead = false;

    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Member recipient;

    @CreatedDate
    private LocalDateTime createdAt;

    public Notification(NotificationType type, String message, Member recipient) {
        this.type = type;
        this.message = message;
        this.recipient = recipient;
    }
}
