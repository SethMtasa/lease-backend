package prac.lease.dto;

import prac.lease.model.Landlord;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LandlordResponseDto {

    private Long id;
    private String fullName;
    private String contactPerson;
    private String contactNumber;
    private String email;
    private List<BankDetailsResponseDto> bankDetails = new ArrayList<>();

    // No-args constructor for deserialization
    public LandlordResponseDto() {
    }

    /**
     * Constructs a DTO from a Landlord entity.
     * This constructor is used to populate the DTO with data from the database.
     * @param landlord The Landlord entity to convert.
     */
    public LandlordResponseDto(Landlord landlord) {
        this.id = landlord.getId();
        this.fullName = landlord.getFullName();
        this.contactPerson = landlord.getContactPerson();
        this.contactNumber = landlord.getContactNumber();
        this.email = landlord.getEmail();

        // Convert bank details entities to DTOs
        if (landlord.getBankDetails() != null) {
            this.bankDetails = landlord.getBankDetails().stream()
                    .map(BankDetailsResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    // All-args constructor
    public LandlordResponseDto(Long id, String fullName, String contactPerson, String contactNumber, String email, List<BankDetailsResponseDto> bankDetails) {
        this.id = id;
        this.fullName = fullName;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.email = email;
        this.bankDetails = bankDetails != null ? bankDetails : new ArrayList<>();
    }

    // Getters
    public Long getId() {
        return id;
    }

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

    public List<BankDetailsResponseDto> getBankDetails() {
        return bankDetails;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

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

    public void setBankDetails(List<BankDetailsResponseDto> bankDetails) {
        this.bankDetails = bankDetails != null ? bankDetails : new ArrayList<>();
    }
}