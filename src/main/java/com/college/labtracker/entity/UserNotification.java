package com.college.labtracker.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_notifications")
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    public UserNotification() {
    }

    public UserNotification(User recipient, String title, String message) {
        this.recipient = recipient;
        this.title = title;
        this.message = message;
    }

    public Long getId() { return id; }
    public User getRecipient() { return recipient; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}