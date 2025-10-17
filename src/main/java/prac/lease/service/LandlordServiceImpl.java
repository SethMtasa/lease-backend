package prac.lease.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.BankDetailsRequest;
import prac.lease.dto.LandlordRequestDto;
import prac.lease.dto.LandlordResponseDto;
import prac.lease.model.BankDetails;
import prac.lease.model.Landlord;
import prac.lease.repository.BankDetailsRepository;
import prac.lease.repository.LandlordRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LandlordServiceImpl implements LandlordService {

    private final LandlordRepository landlordRepository;
    private final BankDetailsRepository bankDetailsRepository;

    // Constructor-based dependency injection
    public LandlordServiceImpl(LandlordRepository landlordRepository, BankDetailsRepository bankDetailsRepository) {
        this.landlordRepository = landlordRepository;
        this.bankDetailsRepository = bankDetailsRepository;
    }

    /**
     * Creates a new landlord record with optional bank details.
     * @param landlordRequest The DTO containing the landlord details and bank details.
     * @return ApiResponse containing the newly created LandlordResponseDto.
     */
    @Override
    @Transactional
    public ApiResponse<LandlordResponseDto> createLandlord(LandlordRequestDto landlordRequest) {
        try {
            // Create a new Landlord entity
            Landlord newLandlord = new Landlord();

            // Set all fields from the request DTO
            newLandlord.setFullName(landlordRequest.getFullName());
            newLandlord.setContactPerson(landlordRequest.getContactPerson());
            newLandlord.setContactNumber(landlordRequest.getContactNumber());
            newLandlord.setEmail(landlordRequest.getEmail());

            // Save the new landlord entity to the database
            Landlord savedLandlord = landlordRepository.save(newLandlord);

            // Process bank details if provided
            if (landlordRequest.getBankDetails() != null && !landlordRequest.getBankDetails().isEmpty()) {
                for (BankDetailsRequest bankDetailRequest : landlordRequest.getBankDetails()) {
                    // Validate bank details don't already exist
                    if (bankDetailsRepository.existsByAccountNumberAndSortCode(
                            bankDetailRequest.getAccountNumber(), bankDetailRequest.getSortCode())) {
                        return new ApiResponse<>(false,
                                "Bank details with account number '" + bankDetailRequest.getAccountNumber() +
                                        "' and sort code '" + bankDetailRequest.getSortCode() + "' already exists.", null);
                    }

                    // Create and save bank details
                    BankDetails bankDetails = new BankDetails();
                    bankDetails.setAccountNumber(bankDetailRequest.getAccountNumber());
                    bankDetails.setSortCode(bankDetailRequest.getSortCode());
                    bankDetails.setBranch(bankDetailRequest.getBranch());
                    bankDetails.setBank(bankDetailRequest.getBank());
                    bankDetails.setAccountName(bankDetailRequest.getAccountName());
                    bankDetails.setAccountType(bankDetailRequest.getAccountType());
                    bankDetails.setLandlord(savedLandlord);

                    BankDetails savedBankDetails = bankDetailsRepository.save(bankDetails);
                    savedLandlord.addBankDetail(savedBankDetails);
                }
            }

            // Return a successful API response with the saved landlord's data
            return new ApiResponse<>(true, "Landlord created successfully", new LandlordResponseDto(savedLandlord));

        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to create landlord: " + e.getMessage(), null);
        }
    }

    /**
     * Retrieves a landlord by their ID including bank details.
     * @param id The ID of the landlord.
     * @return ApiResponse containing the LandlordResponseDto.
     */
    @Override
    public ApiResponse<LandlordResponseDto> getLandlordById(Long id) {
        try {
            Optional<Landlord> optionalLandlord = landlordRepository.findById(id);
            return optionalLandlord.map(landlord ->
                            new ApiResponse<>(true, "Landlord found", new LandlordResponseDto(landlord)))
                    .orElseGet(() -> new ApiResponse<>(false, "Landlord not found with ID: " + id, null));
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to retrieve landlord: " + e.getMessage(), null);
        }
    }

    /**
     * Retrieves a list of all landlords including their bank details.
     * @return ApiResponse containing a list of all LandlordResponseDto.
     */
    @Override
    public ApiResponse<List<LandlordResponseDto>> getAllLandlords() {
        try {
            List<Landlord> landlords = landlordRepository.findAll();
            List<LandlordResponseDto> dtos = landlords.stream()
                    .map(LandlordResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "All landlords retrieved", dtos);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to retrieve landlords: " + e.getMessage(), null);
        }
    }

    /**
     * Updates an existing landlord's details.
     * @param id The ID of the landlord to update.
     * @param updateRequest The DTO with the updated landlord details.
     * @return ApiResponse containing the updated LandlordResponseDto.
     */
    @Override
    @Transactional
    public ApiResponse<LandlordResponseDto> updateLandlord(Long id, LandlordRequestDto updateRequest) {
        try {
            Optional<Landlord> optionalLandlord = landlordRepository.findById(id);
            if (optionalLandlord.isEmpty()) {
                return new ApiResponse<>(false, "Landlord not found with ID: " + id, null);
            }

            Landlord landlordToUpdate = optionalLandlord.get();
            // Update all fields from the request DTO
            landlordToUpdate.setFullName(updateRequest.getFullName());
            landlordToUpdate.setContactPerson(updateRequest.getContactPerson());
            landlordToUpdate.setContactNumber(updateRequest.getContactNumber());
            landlordToUpdate.setEmail(updateRequest.getEmail());

            Landlord updatedLandlord = landlordRepository.save(landlordToUpdate);
            return new ApiResponse<>(true, "Landlord updated successfully", new LandlordResponseDto(updatedLandlord));

        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to update landlord: " + e.getMessage(), null);
        }
    }

    /**
     * Deletes a landlord by their ID (also deletes associated bank details due to cascade).
     * @param id The ID of the landlord to delete.
     * @return ApiResponse indicating success or failure.
     */
    @Override
    @Transactional
    public ApiResponse<String> deleteLandlord(Long id) {
        try {
            if (landlordRepository.existsById(id)) {
                landlordRepository.deleteById(id);
                return new ApiResponse<>(true, "Landlord deleted successfully", null);
            }
            return new ApiResponse<>(false, "Landlord not found with ID: " + id, null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to delete landlord: " + e.getMessage(), null);
        }
    }
}