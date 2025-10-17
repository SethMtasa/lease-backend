package prac.lease.controller;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.LeaseResponseDto;
import prac.lease.dto.ReportRequest;
import prac.lease.dto.ReportResponseDto;
import prac.lease.model.LeaseStatus;
import prac.lease.model.RentalType;
import prac.lease.model.LeaseType;
import prac.lease.service.ReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Existing endpoints...
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<ReportResponseDto>> generateReport(@RequestBody ReportRequest reportRequest) {
        ApiResponse<ReportResponseDto> apiResponse = reportService.generateReport(reportRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReportResponseDto>> getReportById(@PathVariable Long id) {
        ApiResponse<ReportResponseDto> apiResponse = reportService.getReportById(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getAllReports() {
        ApiResponse<List<ReportResponseDto>> apiResponse = reportService.getAllReports();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteReport(@PathVariable Long id) {
        ApiResponse<String> apiResponse = reportService.deleteReport(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    // 5.4.1. Consolidated lease registers by category
    @GetMapping("/consolidated-leases")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getConsolidatedLeaseRegister(
            @RequestParam(required = false) String category) {
        ApiResponse<List<LeaseResponseDto>> apiResponse =
                reportService.getConsolidatedLeaseRegisterByCategory(category);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // 5.4.2. Expired Lease Report
    @GetMapping("/expired-leases")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getExpiredLeasesReport() {
        ApiResponse<List<LeaseResponseDto>> apiResponse = reportService.getExpiredLeasesReport();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // 5.4.3. Lease Expiration Report (Upcoming expirations)
    @GetMapping("/upcoming-expirations")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getUpcomingLeaseExpirations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ApiResponse<List<LeaseResponseDto>> apiResponse =
                reportService.getUpcomingLeaseExpirationsReport(startDate, endDate);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // 5.4.4. Reports schedule by category
    @GetMapping("/category-summary")
    public ResponseEntity<ApiResponse<Map<String, List<LeaseResponseDto>>>> getLeasesByCategoryReport() {
        ApiResponse<Map<String, List<LeaseResponseDto>>> apiResponse =
                reportService.getLeasesByCategoryReport();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // Additional utility endpoints - UPDATED TO USE ACTUAL SERVICE METHODS

    @GetMapping("/by-status/{status}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByStatus(@PathVariable LeaseStatus status) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = reportService.getLeasesByStatus(status);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/by-rental-type/{rentalType}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByRentalType(@PathVariable RentalType rentalType) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = reportService.getLeasesByRentalType(rentalType);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/by-lease-type/{leaseType}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByLeaseType(@PathVariable LeaseType leaseType) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = reportService.getLeasesByLeaseType(leaseType);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/status-summary")
    public ResponseEntity<ApiResponse<Map<LeaseStatus, List<LeaseResponseDto>>>> getLeasesByStatusReport() {
        ApiResponse<Map<LeaseStatus, List<LeaseResponseDto>>> apiResponse = reportService.getLeasesByStatusReport();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/rental-type-summary")
    public ResponseEntity<ApiResponse<Map<RentalType, List<LeaseResponseDto>>>> getLeasesByRentalTypeReport() {
        ApiResponse<Map<RentalType, List<LeaseResponseDto>>> apiResponse = reportService.getLeasesByRentalTypeReport();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/lease-type-summary")
    public ResponseEntity<ApiResponse<Map<LeaseType, List<LeaseResponseDto>>>> getLeasesByLeaseTypeReport() {
        ApiResponse<Map<LeaseType, List<LeaseResponseDto>>> apiResponse = reportService.getLeasesByLeaseTypeReport();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}