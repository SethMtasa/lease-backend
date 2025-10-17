package prac.lease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prac.lease.model.Report;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // Find all reports by their type (e.g., "consolidated_lease_register")
    List<Report> findByReportType(String reportType);

    // Find reports by name
    List<Report> findByReportNameContaining(String reportName);
}