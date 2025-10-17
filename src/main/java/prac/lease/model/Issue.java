package prac.lease.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Audited(withModifiedFlag = true)
public class Issue extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String description;

    // Use the enum type for status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus status;

    @Column(nullable = false)
    private LocalDateTime createdDate; // Field to store the creation timestamp

    @ManyToOne
    private Landlord landlord;

    // No-args constructor for JPA
    public Issue() {
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Landlord getLandlord() {
        return landlord;
    }

    public void setLandlord(Landlord landlord) {
        this.landlord = landlord;
    }
}