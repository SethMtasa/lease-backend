package prac.lease.model;

import jakarta.persistence.*;
import prac.lease.model.BaseEntity;
import prac.lease.model.User;

/**
 * Represents a notification for a user.
 */
@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {
    private String message;
    private boolean isRead = false;

    // A notification is for a specific user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // No-args constructor
    public Notification() {
    }

    // All-args constructor
    public Notification(String message, boolean isRead, User user) {
        this.message = message;
        this.isRead = isRead;
        this.user = user;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }

    public User getUser() {
        return user;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
