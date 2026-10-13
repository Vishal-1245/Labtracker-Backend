package com.college.labtracker.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.college.labtracker.dto.notification.NotificationResponse;
import com.college.labtracker.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Principal principal) {
        return ResponseEntity.ok(notificationService.getMyNotifications(principal.getName()));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationResponse> markRead(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(notificationService.markRead(id, principal.getName()));
    }
}