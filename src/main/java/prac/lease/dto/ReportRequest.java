package prac.lease.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import prac.lease.model.LeaseStatus;
import prac.lease.model.LeaseType;
import prac.lease.model.RentalType;

import java.time.LocalDate;

public record ReportRequest(
        @NotBlank String reportName,
        @NotBlank String reportType,
        LocalDate startDate,  // Made optional for some reports
        LocalDate endDate,    // Made optional for some reports
        String category,
        String leaseCategory, // Specific for lease category reports
        LeaseStatus status,   // For status-based reports
        RentalType rentalType, // For rental type reports
        LeaseType leaseType   // For lease type reports
) {}