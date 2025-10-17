package prac.lease.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Audited(withModifiedFlag = true)
public class Report extends BaseEntity {

    @Column(nullable = false)
    private String reportName;

    @Column
    private String reportType;

    @Column(nullable = false)
    private LocalDateTime generationDate;

    // No-args constructor for JPA
    public Report() {
    }

    // Getters and setters
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDateTime getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDateTime generationDate) {
        this.generationDate = generationDate;
    }
}