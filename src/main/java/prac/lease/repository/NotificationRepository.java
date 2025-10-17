package prac.lease.repository;

// File: NotificationRepository.java
// Package: prac.lease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prac.lease.model.Notification;
import java.util.List;

/**
 * Repository interface for the Notification entity.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserUsername(String username);
}