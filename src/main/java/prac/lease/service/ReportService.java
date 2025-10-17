package prac.lease.service;

import prac.lease.dto.ApiResponse;
import prac.lease.dto.LeaseResponseDto;
import prac.lease.dto.ReportRequest;
import prac.lease.dto.ReportResponseDto;
import prac.lease.model.LeaseStatus;
import prac.lease.model.RentalType;
import prac.lease.model.LeaseType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for generating and retrieving system reports.
 */
public interface ReportService {
    /**
     * Generates a new report based on the provided request parameters.
     * @param reportRequest The DTO with report generation criteria.
     * @return ApiResponse containing the metadata of the generated report.
     */
    ApiResponse<ReportResponseDto> generateReport(ReportRequest reportRequest);

    /**
     * Retrieves a report's metadata by its ID.
     * @param id The ID of the report.
     * @return ApiResponse containing the ReportResponseDto.
     */
    ApiResponse<ReportResponseDto> getReportById(Long id);

    /**
     * Retrieves a list of all generated reports.
     * @return ApiResponse containing a list of all ReportResponseDto.
     */
    ApiResponse<List<ReportResponseDto>> getAllReports();

    /**
     * Deletes a report by its ID.
     * @param id The ID of the report to delete.
     * @return ApiResponse indicating success or failure.
     */
    ApiResponse<String> deleteReport(Long id);

    // 5.4.1. Consolidated lease registers by category
    ApiResponse<List<LeaseResponseDto>> getConsolidatedLeaseRegisterByCategory(String category);

    // 5.4.2. Expired Lease Report
    ApiResponse<List<LeaseResponseDto>> getExpiredLeasesReport();

    // 5.4.3. Lease Expiration Report (Upcoming expirations)
    ApiResponse<List<LeaseResponseDto>> getUpcomingLeaseExpirationsReport(LocalDate startDate, LocalDate endDate);

    // 5.4.4. Reports schedule by category
    ApiResponse<Map<String, List<LeaseResponseDto>>> getLeasesByCategoryReport();

    // Additional report methods for status-based reports
    ApiResponse<Map<LeaseStatus, List<LeaseResponseDto>>> getLeasesByStatusReport();

    // Additional report methods for rental type-based reports
    ApiResponse<Map<RentalType, List<LeaseResponseDto>>> getLeasesByRentalTypeReport();

    // Additional report methods for lease type-based reports
    ApiResponse<Map<LeaseType, List<LeaseResponseDto>>> getLeasesByLeaseTypeReport();

    // Get leases by specific status
    ApiResponse<List<LeaseResponseDto>> getLeasesByStatus(LeaseStatus status);

    // Get leases by rental type
    ApiResponse<List<LeaseResponseDto>> getLeasesByRentalType(RentalType rentalType);

    // Get leases by lease type
    ApiResponse<List<LeaseResponseDto>> getLeasesByLeaseType(LeaseType leaseType);
}