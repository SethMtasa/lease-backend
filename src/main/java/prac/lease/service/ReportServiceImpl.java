package prac.lease.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import prac.lease.dto.*;
import prac.lease.model.*;
import prac.lease.repository.LeaseRepository;
import prac.lease.repository.ReportRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final LeaseRepository leaseRepository;

    public ReportServiceImpl(ReportRepository reportRepository, LeaseRepository leaseRepository) {
        this.reportRepository = reportRepository;
        this.leaseRepository = leaseRepository;
    }

    @Override
    @Transactional
    public ApiResponse<ReportResponseDto> generateReport(ReportRequest reportRequest) {
        try {
            // Generate report based on type
            switch (reportRequest.reportType().toUpperCase()) {
                case "CONSOLIDATED_LEASE_REGISTER":
                    return generateConsolidatedLeaseRegister(reportRequest);
                case "EXPIRED_LEASES":
                    return generateExpiredLeasesReport(reportRequest);
                case "UPCOMING_EXPIRATIONS":
                    return generateUpcomingExpirationsReport(reportRequest);
                case "CATEGORY_SUMMARY":
                    return generateCategorySummaryReport(reportRequest);
                case "STATUS_SUMMARY":
                    return generateStatusSummaryReport(reportRequest);
                case "RENTAL_TYPE_SUMMARY":
                    return generateRentalTypeSummaryReport(reportRequest);
                default:
                    return new ApiResponse<>(false, "Unknown report type: " + reportRequest.reportType(), null);
            }
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error generating report: " + e.getMessage(), null);
        }
    }

    private ApiResponse<ReportResponseDto> generateConsolidatedLeaseRegister(ReportRequest request) {
        List<Lease> leases;

        if (request.leaseCategory() != null && !request.leaseCategory().isEmpty()) {
            // Filter by specific category
            leases = leaseRepository.findByLeaseCategoryContainingIgnoreCase(request.leaseCategory());
        } else if (request.category() != null && !request.category().isEmpty()) {
            // Fallback to category field
            leases = leaseRepository.findByLeaseCategoryContainingIgnoreCase(request.category());
        } else {
            // Get all leases
            leases = leaseRepository.findAll();
        }

        // Save report metadata
        Report report = saveReportMetadata(request, leases.size());

        return new ApiResponse<>(true,
                String.format("Consolidated lease register generated with %d records", leases.size()),
                new ReportResponseDto(report));
    }

    private ApiResponse<ReportResponseDto> generateExpiredLeasesReport(ReportRequest request) {
        LocalDate today = LocalDate.now();
        // For expired leases, we look for leases where expiry date is before today
        // and status is not relevant since expired leases can have any status
        List<Lease> expiredLeases = leaseRepository.findByExpiryDateBefore(today);

        Report report = saveReportMetadata(request, expiredLeases.size());

        return new ApiResponse<>(true,
                String.format("Expired leases report generated with %d records", expiredLeases.size()),
                new ReportResponseDto(report));
    }

    private ApiResponse<ReportResponseDto> generateUpcomingExpirationsReport(ReportRequest request) {
        LocalDate startDate = request.startDate() != null ? request.startDate() : LocalDate.now();
        LocalDate endDate = request.endDate() != null ? request.endDate() : LocalDate.now().plusMonths(3);

        // For upcoming expirations, we include all leases expiring in the date range
        // regardless of status since we want to see all upcoming expirations
        List<Lease> upcomingExpirations = leaseRepository.findByExpiryDateBetween(startDate, endDate);

        Report report = saveReportMetadata(request, upcomingExpirations.size());

        return new ApiResponse<>(true,
                String.format("Upcoming expirations report generated with %d records for period %s to %s",
                        upcomingExpirations.size(), startDate, endDate),
                new ReportResponseDto(report));
    }

    private ApiResponse<ReportResponseDto> generateCategorySummaryReport(ReportRequest request) {
        List<Lease> allLeases = leaseRepository.findAll();

        // Group by category
        Map<String, Long> categorySummary = allLeases.stream()
                .collect(Collectors.groupingBy(
                        lease -> lease.getLeaseCategory() != null && !lease.getLeaseCategory().isEmpty()
                                ? lease.getLeaseCategory()
                                : "Uncategorized",
                        Collectors.counting()
                ));

        Report report = saveReportMetadata(request, categorySummary.size());

        return new ApiResponse<>(true,
                "Category summary report generated with " + categorySummary.size() + " categories",
                new ReportResponseDto(report));
    }

    private ApiResponse<ReportResponseDto> generateStatusSummaryReport(ReportRequest request) {
        List<Lease> allLeases = leaseRepository.findAll();

        // Group by status
        Map<LeaseStatus, Long> statusSummary = allLeases.stream()
                .collect(Collectors.groupingBy(Lease::getStatus, Collectors.counting()));

        Report report = saveReportMetadata(request, statusSummary.size());

        return new ApiResponse<>(true,
                "Status summary report generated with " + statusSummary.size() + " status types",
                new ReportResponseDto(report));
    }

    private ApiResponse<ReportResponseDto> generateRentalTypeSummaryReport(ReportRequest request) {
        List<Lease> allLeases = leaseRepository.findAll();

        // Group by rental type
        Map<RentalType, Long> rentalTypeSummary = allLeases.stream()
                .collect(Collectors.groupingBy(Lease::getRentalType, Collectors.counting()));

        Report report = saveReportMetadata(request, rentalTypeSummary.size());

        return new ApiResponse<>(true,
                "Rental type summary report generated with " + rentalTypeSummary.size() + " rental types",
                new ReportResponseDto(report));
    }

    private Report saveReportMetadata(ReportRequest request, int recordCount) {
        Report report = new Report();
        report.setReportName(request.reportName());
        report.setReportType(request.reportType());
        report.setGenerationDate(LocalDateTime.now());
        return reportRepository.save(report);
    }

    // 5.4.1. Consolidated lease registers by category
    @Override
    public ApiResponse<List<LeaseResponseDto>> getConsolidatedLeaseRegisterByCategory(String category) {
        try {
            List<Lease> leases;
            if (category != null && !category.trim().isEmpty()) {
                leases = leaseRepository.findByLeaseCategoryContainingIgnoreCase(category);
            } else {
                leases = leaseRepository.findAll();
            }

            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true,
                    String.format("Found %d leases for category: %s", responseDtos.size(),
                            category != null ? category : "All"),
                    responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error generating report: " + e.getMessage(), null);
        }
    }

    // 5.4.2. Expired Lease Report
    @Override
    public ApiResponse<List<LeaseResponseDto>> getExpiredLeasesReport() {
        try {
            LocalDate today = LocalDate.now();
            List<Lease> expiredLeases = leaseRepository.findByExpiryDateBefore(today);

            List<LeaseResponseDto> responseDtos = expiredLeases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true,
                    String.format("Found %d expired leases", responseDtos.size()),
                    responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error generating report: " + e.getMessage(), null);
        }
    }

    // 5.4.3. Lease Expiration Report (Upcoming expirations)
    @Override
    public ApiResponse<List<LeaseResponseDto>> getUpcomingLeaseExpirationsReport(LocalDate startDate, LocalDate endDate) {
        try {
            if (startDate == null) startDate = LocalDate.now();
            if (endDate == null) endDate = LocalDate.now().plusMonths(3);

            if (startDate.isAfter(endDate)) {
                return new ApiResponse<>(false, "Start date cannot be after end date", null);
            }

            List<Lease> upcomingLeases = leaseRepository.findByExpiryDateBetween(startDate, endDate);

            List<LeaseResponseDto> responseDtos = upcomingLeases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true,
                    String.format("Found %d leases expiring between %s and %s",
                            responseDtos.size(), startDate, endDate),
                    responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error generating report: " + e.getMessage(), null);
        }
    }

    // 5.4.4. Reports schedule by category
    @Override
    public ApiResponse<Map<String, List<LeaseResponseDto>>> getLeasesByCategoryReport() {
        try {
            List<Lease> allLeases = leaseRepository.findAll();

            Map<String, List<LeaseResponseDto>> categorizedLeases = allLeases.stream()
                    .collect(Collectors.groupingBy(
                            lease -> {
                                if (lease.getLeaseCategory() != null && !lease.getLeaseCategory().isEmpty()) {
                                    return lease.getLeaseCategory();
                                }
                                return "Uncategorized";
                            },
                            Collectors.mapping(LeaseResponseDto::new, Collectors.toList())
                    ));

            return new ApiResponse<>(true,
                    String.format("Categorized %d leases into %d categories",
                            allLeases.size(), categorizedLeases.size()),
                    categorizedLeases);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error generating report: " + e.getMessage(), null);
        }
    }

    // Additional report methods for your specific requirements

    public ApiResponse<Map<LeaseStatus, List<LeaseResponseDto>>> getLeasesByStatusReport() {
        try {
            List<Lease> allLeases = leaseRepository.findAll();

            Map<LeaseStatus, List<LeaseResponseDto>> statusLeases = allLeases.stream()
                    .collect(Collectors.groupingBy(
                            Lease::getStatus,
                            Collectors.mapping(LeaseResponseDto::new, Collectors.toList())
                    ));

            return new ApiResponse<>(true,
                    String.format("Grouped %d leases by %d status types",
                            allLeases.size(), statusLeases.size()),
                    statusLeases);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error generating report: " + e.getMessage(), null);
        }
    }

    public ApiResponse<Map<RentalType, List<LeaseResponseDto>>> getLeasesByRentalTypeReport() {
        try {
            List<Lease> allLeases = leaseRepository.findAll();

            Map<RentalType, List<LeaseResponseDto>> rentalTypeLeases = allLeases.stream()
                    .collect(Collectors.groupingBy(
                            Lease::getRentalType,
                            Collectors.mapping(LeaseResponseDto::new, Collectors.toList())
                    ));

            return new ApiResponse<>(true,
                    String.format("Grouped %d leases by %d rental types",
                            allLeases.size(), rentalTypeLeases.size()),
                    rentalTypeLeases);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error generating report: " + e.getMessage(), null);
        }
    }

    public ApiResponse<Map<LeaseType, List<LeaseResponseDto>>> getLeasesByLeaseTypeReport() {
        try {
            List<Lease> allLeases = leaseRepository.findAll();

            Map<LeaseType, List<LeaseResponseDto>> leaseTypeLeases = allLeases.stream()
                    .collect(Collectors.groupingBy(
                            Lease::getLeaseType,
                            Collectors.mapping(LeaseResponseDto::new, Collectors.toList())
                    ));

            return new ApiResponse<>(true,
                    String.format("Grouped %d leases by %d lease types",
                            allLeases.size(), leaseTypeLeases.size()),
                    leaseTypeLeases);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error generating report: " + e.getMessage(), null);
        }
    }

    // Get leases by specific status
    public ApiResponse<List<LeaseResponseDto>> getLeasesByStatus(LeaseStatus status) {
        try {
            List<Lease> leases = leaseRepository.findByStatus(status);

            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true,
                    String.format("Found %d leases with status: %s", responseDtos.size(), status),
                    responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases: " + e.getMessage(), null);
        }
    }

    // Get leases by rental type
    public ApiResponse<List<LeaseResponseDto>> getLeasesByRentalType(RentalType rentalType) {
        try {
            List<Lease> leases = leaseRepository.findByRentalType(rentalType);

            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true,
                    String.format("Found %d leases with rental type: %s", responseDtos.size(), rentalType),
                    responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases: " + e.getMessage(), null);
        }
    }

    // Get leases by lease type
    public ApiResponse<List<LeaseResponseDto>> getLeasesByLeaseType(LeaseType leaseType) {
        try {
            List<Lease> leases = leaseRepository.findByLeaseType(leaseType);

            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());

            return new ApiResponse<>(true,
                    String.format("Found %d leases with lease type: %s", responseDtos.size(), leaseType),
                    responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases: " + e.getMessage(), null);
        }
    }

    // Existing methods...
    @Override
    public ApiResponse<ReportResponseDto> getReportById(Long id) {
        Optional<Report> optionalReport = reportRepository.findById(id);
        return optionalReport.map(report -> new ApiResponse<>(true, "Report found", new ReportResponseDto(report)))
                .orElseGet(() -> new ApiResponse<>(false, "Report not found with ID: " + id, null));
    }

    @Override
    public ApiResponse<List<ReportResponseDto>> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        List<ReportResponseDto> dtos = reports.stream()
                .map(ReportResponseDto::new)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "All reports retrieved", dtos);
    }

    @Override
    public ApiResponse<String> deleteReport(Long id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return new ApiResponse<>(true, "Report deleted successfully", null);
        }
        return new ApiResponse<>(false, "Report not found with ID: " + id, null);
    }
}