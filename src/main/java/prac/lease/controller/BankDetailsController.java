package prac.lease.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.BankDetailsRequest;
import prac.lease.dto.BankDetailsResponseDto;
import prac.lease.service.BankDetailsService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/bank-details")
public class BankDetailsController {

    private final BankDetailsService bankDetailsService;

    public BankDetailsController(BankDetailsService bankDetailsService) {
        this.bankDetailsService = bankDetailsService;
    }

    /**
     * Create new bank details for a landlord
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BankDetailsResponseDto>> createBankDetails(
            @RequestBody BankDetailsRequest bankDetailsRequest) {
        ApiResponse<BankDetailsResponseDto> apiResponse = bankDetailsService.createBankDetails(bankDetailsRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    /**
     * Get all bank details for a specific landlord
     */
    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<ApiResponse<List<BankDetailsResponseDto>>> getBankDetailsByLandlordId(
            @PathVariable Long landlordId) {
        ApiResponse<List<BankDetailsResponseDto>> apiResponse = bankDetailsService.getBankDetailsByLandlordId(landlordId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Get specific bank details by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankDetailsResponseDto>> getBankDetailsById(@PathVariable Long id) {
        ApiResponse<BankDetailsResponseDto> apiResponse = bankDetailsService.getBankDetailsById(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Update existing bank details
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BankDetailsResponseDto>> updateBankDetails(
            @PathVariable Long id,
            @RequestBody BankDetailsRequest bankDetailsRequest) {
        ApiResponse<BankDetailsResponseDto> apiResponse = bankDetailsService.updateBankDetails(id, bankDetailsRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /**
     * Delete bank details
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBankDetails(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = bankDetailsService.deleteBankDetails(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Get all bank details in the system
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BankDetailsResponseDto>>> getAllBankDetails() {
        ApiResponse<List<BankDetailsResponseDto>> apiResponse = bankDetailsService.getAllBankDetails();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}