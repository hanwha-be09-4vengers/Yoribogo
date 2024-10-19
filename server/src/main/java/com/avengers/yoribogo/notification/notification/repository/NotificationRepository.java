package com.avengers.yoribogo.notification.notification.repository;

import com.avengers.yoribogo.notification.notification.dto.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
