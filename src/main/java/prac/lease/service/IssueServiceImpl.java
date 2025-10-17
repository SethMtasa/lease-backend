package prac.lease.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.IssueRequest;
import prac.lease.dto.IssueResponseDto;
import prac.lease.model.Issue;
import prac.lease.model.Landlord;
import prac.lease.model.IssueStatus; // Import the enum
import prac.lease.repository.IssueRepository;
import prac.lease.repository.LandlordRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final LandlordRepository landlordRepository;

    public IssueServiceImpl(IssueRepository issueRepository, LandlordRepository landlordRepository) {
        this.issueRepository = issueRepository;
        this.landlordRepository = landlordRepository;
    }

    @Override
    @Transactional
    public ApiResponse<IssueResponseDto> createIssue(IssueRequest issueRequest) {
        Optional<Landlord> optionalLandlord = landlordRepository.findById(issueRequest.landlordId());
        if (optionalLandlord.isEmpty()) {
            return new ApiResponse<>(false, "Landlord not found with ID: " + issueRequest.landlordId(), null);
        }

        Issue newIssue = new Issue();
        newIssue.setDescription(issueRequest.description());
        newIssue.setStatus(IssueStatus.OPEN); // Set default status using the enum
        newIssue.setLandlord(optionalLandlord.get());
        newIssue.setCreatedDate(LocalDateTime.now()); // Recommended to set creation date here

        Issue savedIssue = issueRepository.save(newIssue);
        return new ApiResponse<>(true, "Issue created successfully", new IssueResponseDto(savedIssue));
    }

    @Override
    public ApiResponse<IssueResponseDto> getIssueById(Long id) {
        Optional<Issue> optionalIssue = issueRepository.findById(id);
        return optionalIssue.map(issue -> new ApiResponse<>(true, "Issue found", new IssueResponseDto(issue)))
                .orElseGet(() -> new ApiResponse<>(false, "Issue not found with ID: " + id, null));
    }

    @Override
    public ApiResponse<List<IssueResponseDto>> getAllIssues() {
        List<Issue> issues = issueRepository.findAll();
        List<IssueResponseDto> dtos = issues.stream()
                .map(IssueResponseDto::new)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "All issues retrieved", dtos);
    }

    @Override
    @Transactional
    public ApiResponse<IssueResponseDto> updateIssueStatus(Long id, IssueStatus newStatus) {
        Optional<Issue> optionalIssue = issueRepository.findById(id);
        if (optionalIssue.isEmpty()) {
            return new ApiResponse<>(false, "Issue not found with ID: " + id, null);
        }

        Issue issueToUpdate = optionalIssue.get();
        issueToUpdate.setStatus(newStatus); // Use the enum

        Issue updatedIssue = issueRepository.save(issueToUpdate);
        return new ApiResponse<>(true, "Issue status updated successfully", new IssueResponseDto(updatedIssue));
    }

    @Override
    @Transactional
    public ApiResponse<IssueResponseDto> updateIssue(Long id, IssueRequest issueRequest) {
        Optional<Issue> optionalIssue = issueRepository.findById(id);
        if (optionalIssue.isEmpty()) {
            return new ApiResponse<>(false, "Issue not found with ID: " + id, null);
        }

        Optional<Landlord> optionalLandlord = landlordRepository.findById(issueRequest.landlordId());
        if (optionalLandlord.isEmpty()) {
            return new ApiResponse<>(false, "Landlord not found with ID: " + issueRequest.landlordId(), null);
        }

        Issue issueToUpdate = optionalIssue.get();
        issueToUpdate.setLandlord(optionalLandlord.get());
        issueToUpdate.setDescription(issueRequest.description());
//        issueToUpdate.setStatus(issueRequest.status());

        Issue updatedIssue = issueRepository.save(issueToUpdate);
        return new ApiResponse<>(true, "Issue updated successfully", new IssueResponseDto(updatedIssue));
    }


    @Override
    public ApiResponse<List<IssueResponseDto>> getIssuesByLandlord(Long landlordId) {
        List<Issue> issues = issueRepository.findByLandlordId(landlordId);
        List<IssueResponseDto> dtos = issues.stream()
                .map(IssueResponseDto::new)
                .collect(Collectors.toList());
        return new ApiResponse<>(true, "Issues for landlord retrieved", dtos);
    }

    @Override
    @Transactional
    public ApiResponse<Void> deleteIssue(Long id) {
        if (!issueRepository.existsById(id)) {
            return new ApiResponse<>(false, "Issue not found with ID: " + id, null);
        }
        issueRepository.deleteById(id);
        return new ApiResponse<>(true, "Issue deleted successfully", null);
    }

}