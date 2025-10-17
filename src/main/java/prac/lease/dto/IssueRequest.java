package prac.lease.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IssueRequest(
        @NotBlank String description,
        @NotNull Long landlordId
) {}