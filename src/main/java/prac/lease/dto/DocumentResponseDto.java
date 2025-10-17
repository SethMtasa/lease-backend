package prac.lease.dto;

import prac.lease.model.Document;

import java.time.LocalDateTime;

public class DocumentResponseDto {

    private Long id;
    private String documentType;
    private String fileName;
    private String fileUrl;
    private LocalDateTime uploadTime;
    private String category;
    private String description;
    private Long leaseId;
    private String agreementNumber;
    private Long landlordId;
    private String landlordName;
    private LocalDateTime creationTime;
    private LocalDateTime modificationTime;

    public DocumentResponseDto() {
    }

    public DocumentResponseDto(Document document) {
        this.id = document.getId();
        this.documentType = document.getDocumentType();
        this.fileName = document.getFileName();
        this.fileUrl = document.getFileUrl();
        this.uploadTime = document.getUploadTime();
        this.category = document.getCategory();
        this.description = document.getDescription();

        // Lease information
        if (document.getLease() != null) {
            this.leaseId = document.getLease().getId();
            this.agreementNumber = document.getLease().getAgreementNumber();
        }

        // Landlord information
        if (document.getLandlord() != null) {
            this.landlordId = document.getLandlord().getId();
            this.landlordName = document.getLandlord().getFullName();
        }

        this.creationTime = document.getCreationTime();
        this.modificationTime = document.getModificationTime();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
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

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Long getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(Long landlordId) {
        this.landlordId = landlordId;
    }

    public String getLandlordName() {
        return landlordName;
    }

    public void setLandlordName(String landlordName) {
        this.landlordName = landlordName;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(LocalDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }
}