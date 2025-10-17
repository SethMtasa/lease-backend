package prac.lease.service;

import prac.lease.dto.ApiResponse;
import prac.lease.dto.IssueRequest;
import prac.lease.dto.IssueResponseDto;
import prac.lease.model.IssueStatus; // Import the enum
import java.util.List;

/**
 * Service interface for managing issue tracking and dispute resolution.
 */
public interface IssueService {
    /**
     * Creates a new issue and attaches it to a landlord.
     * @param issueRequest The DTO containing the issue details.
     * @return ApiResponse containing the newly created IssueResponseDto.
     */
    ApiResponse<IssueResponseDto> createIssue(IssueRequest issueRequest);

    /**
     * Retrieves an issue by its ID.
     * @param id The ID of the issue.
     * @return ApiResponse containing the IssueResponseDto.
     */
    ApiResponse<IssueResponseDto> getIssueById(Long id);

    /**
     * Retrieves a list of all issues.
     * @return ApiResponse containing a list of all IssueResponseDto.
     */
    ApiResponse<List<IssueResponseDto>> getAllIssues();

    /**
     * Updates the status of an existing issue.
     * @param id The ID of the issue to update.
     * @param newStatus The new status for the issue (e.g., "IN_PROGRESS", "RESOLVED").
     * @return ApiResponse containing the updated IssueResponseDto.
     */
    ApiResponse<IssueResponseDto> updateIssueStatus(Long id, IssueStatus newStatus);


    /**
     * Updates an existing issue with new details.
     * @param id The ID of the issue to update.
     * @param issueRequest The DTO containing the new issue details.
     * @return ApiResponse containing the updated IssueResponseDto.
     */
    ApiResponse<IssueResponseDto> updateIssue(Long id, IssueRequest issueRequest);

    /**
     * Retrieves a list of all issues for a specific landlord.
     * @param landlordId The ID of the landlord.
     * @return ApiResponse containing a list of IssueResponseDto.
     */
    ApiResponse<List<IssueResponseDto>> getIssuesByLandlord(Long landlordId);

    /**
     * Deletes an issue by its ID.
     * @param id The ID of the issue to delete.
     * @return ApiResponse indicating the success or failure of the deletion.
     */
    ApiResponse<Void> deleteIssue(Long id);
}