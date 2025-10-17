package prac.lease.dto;

import prac.lease.model.Issue;
import prac.lease.model.IssueStatus; // Import the enum
import java.time.LocalDateTime;

public class IssueResponseDto {
    private Long id;
    private String description;
    private IssueStatus status; // Changed to enum type
    private LocalDateTime createdDate;
    private LandlordResponseDto landlord;

    // No-args constructor
    public IssueResponseDto() {
    }

    // Constructor from entity
    public IssueResponseDto(Issue issue) {
        this.id = issue.getId();
        this.description = issue.getDescription();
        this.status = issue.getStatus(); // Directly use the enum
        this.createdDate = issue.getCreatedDate();
        if (issue.getLandlord() != null) {
            this.landlord = new LandlordResponseDto(issue.getLandlord());
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueStatus getStatus() { // Getter returns enum
        return status;
    }

    public void setStatus(IssueStatus status) { // Setter accepts enum
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LandlordResponseDto getLandlord() {
        return landlord;
    }

    public void setLandlord(LandlordResponseDto landlord) {
        this.landlord = landlord;
    }
}