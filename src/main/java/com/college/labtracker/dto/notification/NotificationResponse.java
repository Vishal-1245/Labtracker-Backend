package com.college.labtracker.dto.notification;

import java.time.LocalDateTime;

public class NotificationResponse {
    private final Long id;
    private final String title;
    private final String message;
    private final LocalDateTime createdAt;
    private final boolean read;

    public NotificationResponse(Long id, String title, String message, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
        this.read = read;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isRead() { return read; }
}