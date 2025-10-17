package prac.lease.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record for user login requests.
 */
public record LoginRequest(@NotBlank String username, @NotBlank String password) {
}