package prac.lease.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.IssueRequest;
import prac.lease.dto.IssueResponseDto;
import prac.lease.model.IssueStatus;
import prac.lease.service.IssueService;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    /**
     * Creates a new issue and attaches it to a landlord.
     * @param issueRequest The DTO containing the issue details.
     * @return ResponseEntity with the created IssueResponseDto.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<IssueResponseDto>> createIssue(@RequestBody IssueRequest issueRequest) {
        ApiResponse<IssueResponseDto> apiResponse = issueService.createIssue(issueRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves an issue by its ID.
     * @param id The ID of the issue.
     * @return ResponseEntity with the IssueResponseDto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IssueResponseDto>> getIssueById(@PathVariable Long id) {
        ApiResponse<IssueResponseDto> apiResponse = issueService.getIssueById(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Retrieves a list of all issues.
     * @return ResponseEntity with a list of all IssueResponseDto.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<IssueResponseDto>>> getAllIssues() {
        ApiResponse<List<IssueResponseDto>> apiResponse = issueService.getAllIssues();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    /**
     * Updates the status of an existing issue.
     * @param id The ID of the issue to update.
     * @param newStatus The new status for the issue.
     * @return ResponseEntity with the updated IssueResponseDto.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<IssueResponseDto>> updateIssueStatus(@PathVariable Long id, @RequestParam("status") IssueStatus newStatus) {
        ApiResponse<IssueResponseDto> apiResponse = issueService.updateIssueStatus(id, newStatus);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves all issues for a specific landlord.
     * @param landlordId The ID of the landlord.
     * @return ResponseEntity with a list of IssueResponseDto.
     */
    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<ApiResponse<List<IssueResponseDto>>> getIssuesByLandlord(@PathVariable Long landlordId) {
        ApiResponse<List<IssueResponseDto>> apiResponse = issueService.getIssuesByLandlord(landlordId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
    /**
     * Deletes an issue by its ID.
     * @param id The ID of the issue to delete.
     * @return ResponseEntity indicating the success or failure of the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIssue(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = issueService.deleteIssue(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}