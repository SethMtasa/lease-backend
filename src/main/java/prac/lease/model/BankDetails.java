package prac.lease.model;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "bank_details")
public class BankDetails extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String accountNumber;

    @Column(nullable = false, length = 20)
    private String sortCode;

    @Column(length = 100)
    private String branch;

    @Column(nullable = false, length = 100)
    private String bank;

    @Column(length = 255)
    private String accountName;

    @Column(length = 50)
    private String accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landlord_id")
    private Landlord landlord;

    public BankDetails() {
    }

    public BankDetails(String accountNumber, String sortCode, String branch, String bank, String accountName) {
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.branch = branch;
        this.bank = bank;
        this.accountName = accountName;
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public String getBranch() {
        return branch;
    }

    public String getBank() {
        return bank;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public Landlord getLandlord() {
        return landlord;
    }

    // Setters
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setLandlord(Landlord landlord) {
        this.landlord = landlord;
    }

    @Override
    public String toString() {
        return "BankDetails{" +
                "id=" + getId() +
                ", accountNumber='" + accountNumber + '\'' +
                ", sortCode='" + sortCode + '\'' +
                ", branch='" + branch + '\'' +
                ", bank='" + bank + '\'' +
                ", accountName='" + accountName + '\'' +
                '}';
    }
}