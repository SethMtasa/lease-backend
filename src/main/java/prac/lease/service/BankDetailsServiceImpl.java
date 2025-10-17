package prac.lease.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.BankDetailsRequest;
import prac.lease.dto.BankDetailsResponseDto;
import prac.lease.model.BankDetails;
import prac.lease.model.Landlord;
import prac.lease.repository.BankDetailsRepository;
import prac.lease.repository.LandlordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankDetailsServiceImpl implements BankDetailsService {

    private final BankDetailsRepository bankDetailsRepository;
    private final LandlordRepository landlordRepository;

    public BankDetailsServiceImpl(BankDetailsRepository bankDetailsRepository,
                                  LandlordRepository landlordRepository) {
        this.bankDetailsRepository = bankDetailsRepository;
        this.landlordRepository = landlordRepository;
    }

    @Override
    @Transactional
    public ApiResponse<BankDetailsResponseDto> createBankDetails(BankDetailsRequest bankDetailsRequest) {
        try {
            // Validate landlord exists
            Landlord landlord = landlordRepository.findById(bankDetailsRequest.getLandlordId())
                    .orElseThrow(() -> new RuntimeException("Landlord not found with ID: " + bankDetailsRequest.getLandlordId()));

            // Check if bank details with same account number and sort code already exist
            if (bankDetailsRepository.existsByAccountNumberAndSortCode(
                    bankDetailsRequest.getAccountNumber(), bankDetailsRequest.getSortCode())) {
                return new ApiResponse<>(false,
                        "Bank details with account number '" + bankDetailsRequest.getAccountNumber() +
                                "' and sort code '" + bankDetailsRequest.getSortCode() + "' already exists.", null);
            }

            // Create new bank details
            BankDetails bankDetails = new BankDetails();
            bankDetails.setAccountNumber(bankDetailsRequest.getAccountNumber());
            bankDetails.setSortCode(bankDetailsRequest.getSortCode());
            bankDetails.setBranch(bankDetailsRequest.getBranch());
            bankDetails.setBank(bankDetailsRequest.getBank());
            bankDetails.setAccountName(bankDetailsRequest.getAccountName());
            bankDetails.setAccountType(bankDetailsRequest.getAccountType());
            bankDetails.setLandlord(landlord);

            // Save bank details
            BankDetails savedBankDetails = bankDetailsRepository.save(bankDetails);

            // Add to landlord's bank details list
            landlord.getBankDetails().add(savedBankDetails);

            return new ApiResponse<>(true, "Bank details created successfully.",
                    new BankDetailsResponseDto(savedBankDetails));

        } catch (RuntimeException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to create bank details: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<BankDetailsResponseDto>> getBankDetailsByLandlordId(Long landlordId) {
        try {
            // Validate landlord exists
            if (!landlordRepository.existsById(landlordId)) {
                return new ApiResponse<>(false, "Landlord not found with ID: " + landlordId, null);
            }

            List<BankDetails> bankDetailsList = bankDetailsRepository.findByLandlordId(landlordId);
            List<BankDetailsResponseDto> responseDtos = bankDetailsList.stream()
                    .map(BankDetailsResponseDto::new)
                    .collect(Collectors.toList());

            String message = responseDtos.isEmpty() ?
                    "No bank details found for landlord." :
                    "Bank details retrieved successfully.";

            return new ApiResponse<>(true, message, responseDtos);

        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to retrieve bank details: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<BankDetailsResponseDto> getBankDetailsById(Long id) {
        try {
            BankDetails bankDetails = bankDetailsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Bank details not found with ID: " + id));

            return new ApiResponse<>(true, "Bank details retrieved successfully.",
                    new BankDetailsResponseDto(bankDetails));

        } catch (RuntimeException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to retrieve bank details: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<BankDetailsResponseDto> updateBankDetails(Long id, BankDetailsRequest bankDetailsRequest) {
        try {
            BankDetails existingBankDetails = bankDetailsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Bank details not found with ID: " + id));

            // Validate landlord exists if provided
            Landlord landlord = null;
            if (bankDetailsRequest.getLandlordId() != null) {
                landlord = landlordRepository.findById(bankDetailsRequest.getLandlordId())
                        .orElseThrow(() -> new RuntimeException("Landlord not found with ID: " + bankDetailsRequest.getLandlordId()));
            }

            // Check if account number and sort code combination already exists (excluding current record)
            if (bankDetailsRepository.existsByAccountNumberAndSortCodeAndIdNot(
                    bankDetailsRequest.getAccountNumber(), bankDetailsRequest.getSortCode(), id)) {
                return new ApiResponse<>(false,
                        "Bank details with account number '" + bankDetailsRequest.getAccountNumber() +
                                "' and sort code '" + bankDetailsRequest.getSortCode() + "' already exists.", null);
            }

            // Update bank details
            existingBankDetails.setAccountNumber(bankDetailsRequest.getAccountNumber());
            existingBankDetails.setSortCode(bankDetailsRequest.getSortCode());
            existingBankDetails.setBranch(bankDetailsRequest.getBranch());
            existingBankDetails.setBank(bankDetailsRequest.getBank());
            existingBankDetails.setAccountName(bankDetailsRequest.getAccountName());
            existingBankDetails.setAccountType(bankDetailsRequest.getAccountType());

            if (landlord != null) {
                existingBankDetails.setLandlord(landlord);
            }

            BankDetails updatedBankDetails = bankDetailsRepository.save(existingBankDetails);

            return new ApiResponse<>(true, "Bank details updated successfully.",
                    new BankDetailsResponseDto(updatedBankDetails));

        } catch (RuntimeException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to update bank details: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteBankDetails(Long id) {
        try {
            BankDetails bankDetails = bankDetailsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Bank details not found with ID: " + id));

            // Remove from landlord's bank details list
            if (bankDetails.getLandlord() != null) {
                bankDetails.getLandlord().getBankDetails().remove(bankDetails);
            }

            bankDetailsRepository.delete(bankDetails);

            return new ApiResponse<>(true, "Bank details deleted successfully.", null);

        } catch (RuntimeException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to delete bank details: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<List<BankDetailsResponseDto>> getAllBankDetails() {
        try {
            List<BankDetails> bankDetailsList = bankDetailsRepository.findAll();
            List<BankDetailsResponseDto> responseDtos = bankDetailsList.stream()
                    .map(BankDetailsResponseDto::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true, "All bank details retrieved successfully.", responseDtos);

        } catch (Exception e) {
            return new ApiResponse<>(false, "Failed to retrieve bank details: " + e.getMessage(), null);
        }
    }
}