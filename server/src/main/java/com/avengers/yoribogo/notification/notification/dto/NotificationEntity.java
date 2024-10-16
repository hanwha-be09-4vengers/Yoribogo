package com.avengers.yoribogo.notification.notification.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(nullable = false)
    private String notificationContent;

    @Column(nullable = false)
    private String notificationReadStatus;

    @Column(nullable = false)
    private LocalDateTime notificationCreatedAt;

    private LocalDateTime notificationReadAt;

    @Column(nullable = false)
    private Long userId;

}