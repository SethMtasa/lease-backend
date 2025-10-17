package prac.lease.dto;

import jakarta.validation.constraints.NotBlank;

public class DocumentRequestDto {

    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "Category is required")
    private String category;

    private String description;

    private Long leaseId; // Optional: for lease-specific documents

    private Long landlordId; // Optional: for landlord-specific documents

    // Getters and Setters
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(Long leaseId) {
        this.leaseId = leaseId;
    }

    public Long getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(Long landlordId) {
        this.landlordId = landlordId;
    }
}