package prac.lease.service;

import prac.lease.dto.ApiResponse;
import prac.lease.dto.BankDetailsRequest;
import prac.lease.dto.BankDetailsResponseDto;

import java.util.List;

public interface BankDetailsService {

    /**
     * Create new bank details for a landlord
     */
    ApiResponse<BankDetailsResponseDto> createBankDetails(BankDetailsRequest bankDetailsRequest);

    /**
     * Get all bank details for a specific landlord
     */
    ApiResponse<List<BankDetailsResponseDto>> getBankDetailsByLandlordId(Long landlordId);

    /**
     * Get specific bank details by ID
     */
    ApiResponse<BankDetailsResponseDto> getBankDetailsById(Long id);

    /**
     * Update existing bank details
     */
    ApiResponse<BankDetailsResponseDto> updateBankDetails(Long id, BankDetailsRequest bankDetailsRequest);

    /**
     * Delete bank details
     */
    ApiResponse<Void> deleteBankDetails(Long id);

    /**
     * Get all bank details in the system
     */
    ApiResponse<List<BankDetailsResponseDto>> getAllBankDetails();
}