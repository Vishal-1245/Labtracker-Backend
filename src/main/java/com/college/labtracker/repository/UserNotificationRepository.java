package com.college.labtracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.college.labtracker.entity.UserNotification;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    List<UserNotification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}