package prac.lease.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Audited(targetAuditMode = NOT_AUDITED, withModifiedFlag = true)
public class Lease extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String agreementNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_id", nullable = false)
    @JsonBackReference
    private Landlord landlord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    @JsonBackReference
    private Site site;

    @Column(nullable = false)
    private LocalDate commencementDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaseStatus status = LeaseStatus.PENDING_APPROVAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalType rentalType;

    private String rentalValue;

    private String commencementAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaseType leaseType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationalStatus operationalStatus;

    private boolean autoRenewalOption;

    private String terminationClauseDetails;

    private String leaseCategory;

    private Integer renewalPeriodMonths;

    @OneToMany(mappedBy = "lease", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("lease-documents")
    private List<Document> documents = new ArrayList<>();

    public Lease() {
    }

    // Business logic methods
    public boolean canAttachDocuments() {
        return this.status == LeaseStatus.APPROVED ||
                this.status == LeaseStatus.ACTIVE ||
                this.status == LeaseStatus.AUTO_RENEWED;
    }

    public void autoRenew() {
        if (this.autoRenewalOption && this.renewalPeriodMonths != null) {
            LocalDate newCommencementDate = this.expiryDate.plusDays(1);
            this.commencementDate = newCommencementDate;
            this.expiryDate = newCommencementDate.plusMonths(this.renewalPeriodMonths);
            this.status = LeaseStatus.AUTO_RENEWED;
        }
    }

    public boolean isExpiringSoon() {
        return LocalDate.now().plusDays(30).isAfter(this.expiryDate) &&
                LocalDate.now().isBefore(this.expiryDate);
    }

    // Getters and Setters
    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Landlord getLandlord() {
        return landlord;
    }

    public void setLandlord(Landlord landlord) {
        this.landlord = landlord;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
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

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void addDocument(Document document) {
        documents.add(document);
        document.setLease(this);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
        document.setLease(null);
    }
}