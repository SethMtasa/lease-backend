package prac.lease.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Audited(targetAuditMode = NOT_AUDITED, withModifiedFlag = true)
public class Landlord extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    private String contactPerson;
    private String contactNumber;
    private String email;

    @OneToMany(mappedBy = "landlord", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("landlord-bankdetails")
    private List<BankDetails> bankDetails = new ArrayList<>();

    @OneToMany(mappedBy = "landlord", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("landlord-issues")
    private List<Issue> issues = new ArrayList<>();

    @OneToMany(mappedBy = "landlord", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("landlord-documents")
    private List<Document> documents = new ArrayList<>();

    @OneToMany(mappedBy = "landlord", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("landlord-leases")
    private List<Lease> leases = new ArrayList<>();

    public Landlord() {
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public List<BankDetails> getBankDetails() {
        return bankDetails;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Lease> getLeases() {
        return leases;
    }

    // Setters
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBankDetails(List<BankDetails> bankDetails) {
        this.bankDetails = bankDetails;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void setLeases(List<Lease> leases) {
        this.leases = leases;
    }

    // Helper methods for managing bank details
    public void addBankDetail(BankDetails bankDetail) {
        bankDetails.add(bankDetail);
        bankDetail.setLandlord(this);
    }

    public void removeBankDetail(BankDetails bankDetail) {
        bankDetails.remove(bankDetail);
        bankDetail.setLandlord(null);
    }

    // Helper methods for managing issues
    public void addIssue(Issue issue) {
        issues.add(issue);
        issue.setLandlord(this);
    }

    public void removeIssue(Issue issue) {
        issues.remove(issue);
        issue.setLandlord(null);
    }

    // Helper methods for managing documents
    public void addDocument(Document document) {
        documents.add(document);
        document.setLandlord(this);
    }

    public void removeDocument(Document document) {
        documents.remove(document);
        document.setLandlord(null);
    }

    // Helper methods for managing leases
    public void addLease(Lease lease) {
        leases.add(lease);
        lease.setLandlord(this);
    }

    public void removeLease(Lease lease) {
        leases.remove(lease);
        lease.setLandlord(null);
    }
}