package prac.lease.dto;

import lombok.Getter;
import lombok.Setter;
import prac.lease.model.Lease;
import prac.lease.model.LeaseStatus;
import prac.lease.model.LeaseType;
import prac.lease.model.OperationalStatus;
import prac.lease.model.RentalType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaseResponseDto {

    private Long id;
    private String agreementNumber;
    private LandlordInfo landlord;
    private SiteInfo site;
    private LocalDate commencementDate;
    private LocalDate expiryDate;
    private LeaseStatus status;
    private RentalType rentalType;
    private String rentalValue;
    private String commencementAmount;
    private LeaseType leaseType;
    private OperationalStatus operationalStatus;
    private boolean autoRenewalOption;
    private String terminationClauseDetails;
    private String leaseCategory;
    private Integer renewalPeriodMonths;
    private LocalDateTime creationTime;
    private LocalDateTime modificationTime;
    private boolean canAttachDocuments;
    private boolean isExpiringSoon;
    private int documentCount;

    public LeaseResponseDto() {
    }

    public LeaseResponseDto(Lease lease) {
        this.id = lease.getId();
        this.agreementNumber = lease.getAgreementNumber();

        // Landlord information as object
        if (lease.getLandlord() != null) {
            this.landlord = new LandlordInfo(lease.getLandlord());
        }

        // Site information as object
        if (lease.getSite() != null) {
            this.site = new SiteInfo(lease.getSite());
        }

        this.commencementDate = lease.getCommencementDate();
        this.expiryDate = lease.getExpiryDate();
        this.status = lease.getStatus();
        this.rentalType = lease.getRentalType();
        this.rentalValue = lease.getRentalValue();
        this.commencementAmount = lease.getCommencementAmount();
        this.leaseType = lease.getLeaseType();
        this.operationalStatus = lease.getOperationalStatus();
        this.autoRenewalOption = lease.isAutoRenewalOption();
        this.terminationClauseDetails = lease.getTerminationClauseDetails();
        this.leaseCategory = lease.getLeaseCategory();
        this.renewalPeriodMonths = lease.getRenewalPeriodMonths();
        this.creationTime = lease.getCreationTime();
        this.modificationTime = lease.getModificationTime();
        this.canAttachDocuments = lease.canAttachDocuments();
        this.isExpiringSoon = lease.isExpiringSoon();
        this.documentCount = lease.getDocuments() != null ? lease.getDocuments().size() : 0;
    }

    // Getters and Setters for main class
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public LandlordInfo getLandlord() {
        return landlord;
    }

    public void setLandlord(LandlordInfo landlord) {
        this.landlord = landlord;
    }

    public SiteInfo getSite() {
        return site;
    }

    public void setSite(SiteInfo site) {
        this.site = site;
    }

    public LocalDate getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(LocalDate commencementDate) {
        this.commencementDate = commencementDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LeaseStatus getStatus() {
        return status;
    }

    public void setStatus(LeaseStatus status) {
        this.status = status;
    }

    public RentalType getRentalType() {
        return rentalType;
    }

    public void setRentalType(RentalType rentalType) {
        this.rentalType = rentalType;
    }

    public String getRentalValue() {
        return rentalValue;
    }

    public void setRentalValue(String rentalValue) {
        this.rentalValue = rentalValue;
    }

    public String getCommencementAmount() {
        return commencementAmount;
    }

    public void setCommencementAmount(String commencementAmount) {
        this.commencementAmount = commencementAmount;
    }

    public LeaseType getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(LeaseType leaseType) {
        this.leaseType = leaseType;
    }

    public OperationalStatus getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(OperationalStatus operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public boolean isAutoRenewalOption() {
        return autoRenewalOption;
    }

    public void setAutoRenewalOption(boolean autoRenewalOption) {
        this.autoRenewalOption = autoRenewalOption;
    }

    public String getTerminationClauseDetails() {
        return terminationClauseDetails;
    }

    public void setTerminationClauseDetails(String terminationClauseDetails) {
        this.terminationClauseDetails = terminationClauseDetails;
    }

    public String getLeaseCategory() {
        return leaseCategory;
    }

    public void setLeaseCategory(String leaseCategory) {
        this.leaseCategory = leaseCategory;
    }

    public Integer getRenewalPeriodMonths() {
        return renewalPeriodMonths;
    }

    public void setRenewalPeriodMonths(Integer renewalPeriodMonths) {
        this.renewalPeriodMonths = renewalPeriodMonths;
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

    public boolean isCanAttachDocuments() {
        return canAttachDocuments;
    }

    public void setCanAttachDocuments(boolean canAttachDocuments) {
        this.canAttachDocuments = canAttachDocuments;
    }

    public boolean isExpiringSoon() {
        return isExpiringSoon;
    }

    public void setExpiringSoon(boolean expiringSoon) {
        isExpiringSoon = expiringSoon;
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public void setDocumentCount(int documentCount) {
        this.documentCount = documentCount;
    }

    // Inner classes with Lombok annotations
    @Getter
    @Setter
    public static class LandlordInfo {
        private Long id;
        private String fullName;
        private String contactPerson;
        private String contactNumber;
        private String email;

        public LandlordInfo() {
        }

        public LandlordInfo(prac.lease.model.Landlord landlord) {
            if (landlord != null) {
                this.id = landlord.getId();
                this.fullName = landlord.getFullName(); // Assuming getName() returns fullName
                this.contactPerson = landlord.getContactPerson();
                this.contactNumber = landlord.getContactNumber(); // Assuming getPhoneNumber() returns contactNumber
                this.email = landlord.getEmail();
            }
        }
    }

    @Getter
    @Setter
    public static class SiteInfo {
        private Long id;
        private String siteName;
        private String province;
        private String district;
        private String zone;

        public SiteInfo() {
        }

        public SiteInfo(prac.lease.model.Site site) {
            if (site != null) {
                this.id = site.getId();
                this.siteName = site.getSiteName(); // Assuming getName() returns siteName
                this.province = site.getProvince();
                this.district = site.getDistrict();
                this.zone = site.getZone();
            }
        }
    }
}