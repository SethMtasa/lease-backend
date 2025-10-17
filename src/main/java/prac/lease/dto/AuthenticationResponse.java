package prac.lease.dto;

/**
 * Record for JWT authentication responses.
 */
public record AuthenticationResponse<String>(boolean success, String message, String token) {
}