package prac.lease.dto;

import prac.lease.model.BankDetails;

public class BankDetailsResponseDto {
    private Long id;
    private String accountNumber;
    private String sortCode;
    private String branch;
    private String bank;
    private String accountName;
    private String accountType;
    private Long landlordId;
    private String landlordName;

    public BankDetailsResponseDto() {
    }

    public BankDetailsResponseDto(Long id, String accountNumber, String sortCode, String branch, String bank, String accountName, String accountType, Long landlordId, String landlordName) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
        this.branch = branch;
        this.bank = bank;
        this.accountName = accountName;
        this.accountType = accountType;
        this.landlordId = landlordId;
        this.landlordName = landlordName;
    }

    public BankDetailsResponseDto(BankDetails bankDetails) {
        this.id = bankDetails.getId();
        this.accountNumber = bankDetails.getAccountNumber();
        this.sortCode = bankDetails.getSortCode();
        this.branch = bankDetails.getBranch();
        this.bank = bankDetails.getBank();
        this.accountName = bankDetails.getAccountName();
        this.accountType = bankDetails.getAccountType();
        this.landlordId = bankDetails.getLandlord() != null ? bankDetails.getLandlord().getId() : null;
        this.landlordName = bankDetails.getLandlord() != null ? bankDetails.getLandlord().getFullName() : null;
    }

    // Getters
    public Long getId() {
        return id;
    }

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

    public String getLandlordName() {
        return landlordName;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

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

    public void setLandlordName(String landlordName) {
        this.landlordName = landlordName;
    }
}