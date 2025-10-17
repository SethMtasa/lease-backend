package prac.lease.dto;

public class BankDetailsRequest {
    private String accountNumber;
    private String sortCode;
    private String branch;
    private String bank;
    private String accountName;
    private String accountType;
    private Long landlordId;

    public BankDetailsRequest() {
    }

    public BankDetailsRequest(String accountNumber, String sortCode, String branch, String bank, String accountName, String accountType, Long landlordId) {
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.branch = branch;
        this.bank = bank;
        this.accountName = accountName;
        this.accountType = accountType;
        this.landlordId = landlordId;
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

    public Long getLandlordId() {
        return landlordId;
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

    public void setLandlordId(Long landlordId) {
        this.landlordId = landlordId;
    }
}