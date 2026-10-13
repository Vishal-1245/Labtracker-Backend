package com.college.labtracker.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.notification.NotificationResponse;
import com.college.labtracker.entity.Booking;
import com.college.labtracker.entity.Role;
import com.college.labtracker.entity.User;
import com.college.labtracker.entity.UserNotification;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.repository.UserNotificationRepository;
import com.college.labtracker.repository.UserRepository;

@Service
public class NotificationService {

    private final UserNotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(UserNotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void notifyBookingCreated(Booking booking) {
        List<User> recipients = userRepository.findAll().stream()
                .filter(User::isActive)
                .filter(user -> user.getRole() == Role.ADMIN
                        || ((user.getRole() == Role.LAB_INCHARGE || user.getRole() == Role.LAB_ASSISTANT)
                                && user.getLab() != null
                                && user.getLab().getId().equals(booking.getLab().getId())))
                .collect(Collectors.toList());

        String experimentTitle = booking.getExperiment() == null ? booking.getPurpose() : booking.getExperiment().getTitle();
        String message = booking.getStudent().getFullName() + " booked " + booking.getLab().getName()
                + " for " + experimentTitle + " on " + booking.getBookingDate()
                + " from " + booking.getStartTime() + " to " + booking.getEndTime() + ".";
        notificationRepository.saveAll(recipients.stream()
                .map(user -> new UserNotification(user, "New lab booking", message))
                .collect(Collectors.toList()));
    }

    public List<NotificationResponse> getMyNotifications(String username) {
        User recipient = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipient.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponse markRead(Long id, String username) {
        UserNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
        if (!notification.getRecipient().getUsername().equals(username)) {
            throw new AccessDeniedException("You cannot update another user's notification");
        }
        notification.setRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    private NotificationResponse toResponse(UserNotification notification) {
        return new NotificationResponse(notification.getId(), notification.getTitle(), notification.getMessage(),
                notification.getCreatedAt(), notification.isRead());
    }
}