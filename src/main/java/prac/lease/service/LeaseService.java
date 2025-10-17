package prac.lease.service;

import org.springframework.web.multipart.MultipartFile;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.DocumentRequestDto;
import prac.lease.dto.LeaseRequestDto;
import prac.lease.dto.LeaseResponseDto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface LeaseService {

    // Basic CRUD operations
    ApiResponse<LeaseResponseDto> createLease(LeaseRequestDto leaseRequest);

    ApiResponse<LeaseResponseDto> getLeaseById(Long id);

    ApiResponse<List<LeaseResponseDto>> getAllLeases();

    ApiResponse<LeaseResponseDto> updateLease(Long id, LeaseRequestDto updateRequest);

    ApiResponse<String> deleteLease(Long id);

    // Approval workflow operations
    ApiResponse<LeaseResponseDto> approveLease(Long id);

    ApiResponse<LeaseResponseDto> rejectLease(Long id, String reason);

    ApiResponse<List<LeaseResponseDto>> getPendingApprovalLeases();

    ApiResponse<List<LeaseResponseDto>> getApprovedLeases();

    // Filtering and search operations
    ApiResponse<List<LeaseResponseDto>> getLeasesByOperationalStatus(String operationalStatus);

    ApiResponse<List<LeaseResponseDto>> getLeasesByLandlordId(Long landlordId);

    ApiResponse<List<LeaseResponseDto>> getLeasesBySiteId(Long siteId);

    ApiResponse<List<LeaseResponseDto>> getLeasesByCategory(String category);

    ApiResponse<List<LeaseResponseDto>> getLeasesByCategoryAndStatus(String category, String status);

    ApiResponse<List<LeaseResponseDto>> getExpiringLeases(LocalDate startDate, LocalDate endDate);

    ApiResponse<List<LeaseResponseDto>> getExpiredLeases();

    // Statistics and reporting operations
    ApiResponse<Map<String, Object>> getLeaseStatistics();

    ApiResponse<Map<String, Object>> getLandlordLeaseStatistics(Long landlordId);

    ApiResponse<Map<String, Object>> getExpiryStatistics();

    // Auto-renewal operations
    void processAutoRenewals();

    // Additional filtering methods
    ApiResponse<List<LeaseResponseDto>> getLeasesByStatus(String status);

    ApiResponse<List<LeaseResponseDto>> getLeasesByRentalType(String rentalType);

    ApiResponse<List<LeaseResponseDto>> getLeasesByLeaseType(String leaseType);

    ApiResponse<List<LeaseResponseDto>> getActiveLeases();

    ApiResponse<List<LeaseResponseDto>> getLeasesWithDocuments();

    ApiResponse<List<LeaseResponseDto>> getLeasesWithoutDocuments();

    ApiResponse<List<LeaseResponseDto>> getLeasesByAutoRenewalOption(boolean autoRenewal);

    // Advanced search with multiple criteria
    ApiResponse<List<LeaseResponseDto>> searchLeases(String agreementNumber, String status,
                                                     String operationalStatus, Long landlordId, Long siteId);

    // SEPARATE DOCUMENT OPERATION
    ApiResponse<LeaseResponseDto> addDocumentsToLease(Long leaseId, List<MultipartFile> files,
                                                      List<DocumentRequestDto> documentRequests) throws IOException;
    // Count operations
    ApiResponse<Long> getTotalLeaseCount();

    ApiResponse<Long> getLeaseCountByStatus(String status);

    ApiResponse<Long> getLeaseCountByOperationalStatus(String operationalStatus);

    // File operations (if needed for initial lease document)
    ApiResponse<LeaseResponseDto> uploadLeaseWithFile(MultipartFile file, LeaseRequestDto leaseRequestDto) throws IOException;

    byte[] downloadLeaseFile(Long leaseId) throws IOException;
}