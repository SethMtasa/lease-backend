package prac.lease.dto;

import java.util.ArrayList;
import java.util.List;

public class LandlordRequestDto {

    private String fullName;
    private String contactPerson;
    private String contactNumber;
    private String email;
    private List<BankDetailsRequest> bankDetails = new ArrayList<>();

    // No-args constructor for deserialization
    public LandlordRequestDto() {
    }

    // All-args constructor
    public LandlordRequestDto(String fullName, String contactPerson, String contactNumber, String email, List<BankDetailsRequest> bankDetails) {
        this.fullName = fullName;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.email = email;
        this.bankDetails = bankDetails != null ? bankDetails : new ArrayList<>();
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

    public List<BankDetailsRequest> getBankDetails() {
        return bankDetails;
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

    public void setBankDetails(List<BankDetailsRequest> bankDetails) {
        this.bankDetails = bankDetails != null ? bankDetails : new ArrayList<>();
    }
}