package prac.lease.dto;

import prac.lease.model.Report;

import java.time.LocalDateTime;

public class ReportResponseDto {
    private Long id;
    private String reportName;
    private String reportType;
    private LocalDateTime generationDate;

    // No-args constructor
    public ReportResponseDto() {
    }

    // Constructor from entity
    public ReportResponseDto(Report report) {
        this.id = report.getId();
        this.reportName = report.getReportName();
        this.reportType = report.getReportType();
        this.generationDate = report.getGenerationDate();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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