package prac.lease.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import prac.lease.model.LeaseType;
import prac.lease.model.OperationalStatus;
import prac.lease.model.RentalType;

import java.time.LocalDate;

public class LeaseRequestDto {

    @NotBlank(message = "Agreement number is required")
    private String agreementNumber;

    @NotNull(message = "Landlord ID is required")
    private Long landlordId;

    @NotNull(message = "Site ID is required")
    private Long siteId;

    @NotNull(message = "Commencement date is required")
    private LocalDate commencementDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    @NotNull(message = "Rental type is required")
    private RentalType rentalType;

    private String rentalValue;

    private String commencementAmount;

    @NotNull(message = "Lease type is required")
    private LeaseType leaseType;

    @NotNull(message = "Operational status is required")
    private OperationalStatus operationalStatus;

    private boolean autoRenewalOption;

    private String terminationClauseDetails;

    private String leaseCategory;

    private Integer renewalPeriodMonths;

    // REMOVED: private List<DocumentRequestDto> documents;

    // Getters and Setters (without documents)
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

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
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
}