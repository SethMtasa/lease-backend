package prac.lease.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.DocumentRequestDto;
import prac.lease.dto.LeaseRequestDto;
import prac.lease.dto.LeaseResponseDto;
import prac.lease.exception.ResourceNotFoundException;
import prac.lease.model.*;
import prac.lease.repository.LeaseRepository;
import prac.lease.repository.LandlordRepository;
import prac.lease.repository.SiteRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LeaseServiceImpl implements LeaseService {

    private final LeaseRepository leaseRepository;
    private final LandlordRepository landlordRepository;
    private final SiteRepository siteRepository;

    // Add this field to resolve fileUploadBaseDir issue
    @Value("${file.upload.path}")
    private String fileUploadBaseDir;

    public LeaseServiceImpl(LeaseRepository leaseRepository,
                            LandlordRepository landlordRepository,
                            SiteRepository siteRepository) {
        this.leaseRepository = leaseRepository;
        this.landlordRepository = landlordRepository;
        this.siteRepository = siteRepository;
    }

    @Override
    @Transactional
    public ApiResponse<LeaseResponseDto> createLease(LeaseRequestDto leaseRequest) {
        try {
            // Check for existing agreement number
            if (leaseRepository.existsByAgreementNumber(leaseRequest.getAgreementNumber())) {
                return new ApiResponse<>(false, "A lease with this agreement number already exists.", null);
            }

            Landlord landlord = landlordRepository.findById(leaseRequest.getLandlordId())
                    .orElseThrow(() -> new ResourceNotFoundException("Landlord not found with ID: " + leaseRequest.getLandlordId()));
            Site site = siteRepository.findById(leaseRequest.getSiteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Site not found with ID: " + leaseRequest.getSiteId()));

            // Validate dates
            if (leaseRequest.getCommencementDate().isAfter(leaseRequest.getExpiryDate())) {
                return new ApiResponse<>(false, "Commencement date cannot be after expiry date.", null);
            }

            Lease newLease = new Lease();
            newLease.setLandlord(landlord);
            newLease.setSite(site);
            newLease.setAgreementNumber(leaseRequest.getAgreementNumber());
            newLease.setCommencementDate(leaseRequest.getCommencementDate());
            newLease.setExpiryDate(leaseRequest.getExpiryDate());
            newLease.setStatus(LeaseStatus.PENDING_APPROVAL);
            newLease.setLeaseType(leaseRequest.getLeaseType());
            newLease.setOperationalStatus(leaseRequest.getOperationalStatus());
            newLease.setAutoRenewalOption(leaseRequest.isAutoRenewalOption());
            newLease.setTerminationClauseDetails(leaseRequest.getTerminationClauseDetails());
            newLease.setLeaseCategory(leaseRequest.getLeaseCategory());
            newLease.setRenewalPeriodMonths(leaseRequest.getRenewalPeriodMonths());

            // Handle rental type logic
            newLease.setRentalType(leaseRequest.getRentalType());
            if (leaseRequest.getRentalType() == RentalType.NONE) {
                newLease.setRentalValue("NONE");
            } else if (leaseRequest.getRentalType() == RentalType.SWAP) {
                newLease.setRentalValue("SWAP");
            } else if (leaseRequest.getRentalType() == RentalType.ANNUALY || leaseRequest.getRentalType() == RentalType.MONTHLY) {
                if (leaseRequest.getRentalValue() == null || leaseRequest.getRentalValue().trim().isEmpty()) {
                    return new ApiResponse<>(false, "Rental value cannot be empty for " + leaseRequest.getRentalType() + " rental type.", null);
                }
                newLease.setRentalValue(leaseRequest.getRentalValue());
            } else {
                newLease.setRentalValue(null);
            }

            newLease.setCommencementAmount(leaseRequest.getCommencementAmount());

            Lease savedLease = leaseRepository.save(newLease);
            LeaseResponseDto responseDto = new LeaseResponseDto(savedLease);

            return new ApiResponse<>(true, "Lease created successfully and pending approval.", responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error creating lease.", null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<LeaseResponseDto> approveLease(Long id) {
        try {
            Lease lease = leaseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lease not found with ID: " + id));

            if (lease.getStatus() != LeaseStatus.PENDING_APPROVAL) {
                return new ApiResponse<>(false, "Lease is not in pending approval status.", null);
            }

            lease.setStatus(LeaseStatus.APPROVED);
            Lease updatedLease = leaseRepository.save(lease);
            LeaseResponseDto responseDto = new LeaseResponseDto(updatedLease);

            return new ApiResponse<>(true, "Lease approved successfully.", responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error approving lease.", null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<LeaseResponseDto> rejectLease(Long id, String reason) {
        try {
            Lease lease = leaseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lease not found with ID: " + id));

            if (lease.getStatus() != LeaseStatus.PENDING_APPROVAL) {
                return new ApiResponse<>(false, "Lease is not in pending approval status.", null);
            }

            lease.setStatus(LeaseStatus.REJECTED);
            Lease updatedLease = leaseRepository.save(lease);
            LeaseResponseDto responseDto = new LeaseResponseDto(updatedLease);

            return new ApiResponse<>(true, "Lease rejected successfully. Reason: " + reason, responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error rejecting lease.", null);
        }
    }

    @Override
    public ApiResponse<LeaseResponseDto> getLeaseById(Long id) {
        try {
            Lease lease = leaseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lease not found with ID: " + id));
            LeaseResponseDto responseDto = new LeaseResponseDto(lease);
            return new ApiResponse<>(true, "Lease retrieved successfully.", responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching lease.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getAllLeases() {
        try {
            List<Lease> leases = leaseRepository.findAll();
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getPendingApprovalLeases() {
        try {
            List<Lease> pendingLeases = leaseRepository.findByStatus(LeaseStatus.PENDING_APPROVAL);
            List<LeaseResponseDto> responseDtos = pendingLeases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Pending approval leases retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching pending approval leases.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getApprovedLeases() {
        try {
            List<Lease> approvedLeases = leaseRepository.findByStatus(LeaseStatus.APPROVED);
            List<LeaseResponseDto> responseDtos = approvedLeases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Approved leases retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching approved leases.", null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<LeaseResponseDto> updateLease(Long id, LeaseRequestDto updateRequest) {
        try {
            Lease existingLease = leaseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lease not found with ID: " + id));

            // Only allow updates for pending or approved leases
            if (existingLease.getStatus() == LeaseStatus.REJECTED || existingLease.getStatus() == LeaseStatus.EXPIRED) {
                return new ApiResponse<>(false, "Cannot update a " + existingLease.getStatus() + " lease.", null);
            }

            Landlord landlord = landlordRepository.findById(updateRequest.getLandlordId())
                    .orElseThrow(() -> new ResourceNotFoundException("Landlord not found with ID: " + updateRequest.getLandlordId()));
            Site site = siteRepository.findById(updateRequest.getSiteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Site not found with ID: " + updateRequest.getSiteId()));

            // Validate dates
            if (updateRequest.getCommencementDate().isAfter(updateRequest.getExpiryDate())) {
                return new ApiResponse<>(false, "Commencement date cannot be after expiry date.", null);
            }

            existingLease.setLandlord(landlord);
            existingLease.setSite(site);
            existingLease.setAgreementNumber(updateRequest.getAgreementNumber());
            existingLease.setCommencementDate(updateRequest.getCommencementDate());
            existingLease.setExpiryDate(updateRequest.getExpiryDate());
            existingLease.setLeaseType(updateRequest.getLeaseType());
            existingLease.setOperationalStatus(updateRequest.getOperationalStatus());
            existingLease.setAutoRenewalOption(updateRequest.isAutoRenewalOption());
            existingLease.setTerminationClauseDetails(updateRequest.getTerminationClauseDetails());
            existingLease.setLeaseCategory(updateRequest.getLeaseCategory());
            existingLease.setRenewalPeriodMonths(updateRequest.getRenewalPeriodMonths());

            // Handle rental type logic
            existingLease.setRentalType(updateRequest.getRentalType());
            if (updateRequest.getRentalType() == RentalType.NONE) {
                existingLease.setRentalValue("NONE");
            } else if (updateRequest.getRentalType() == RentalType.SWAP) {
                existingLease.setRentalValue("SWAP");
            } else if (updateRequest.getRentalType() == RentalType.ANNUALY || updateRequest.getRentalType() == RentalType.MONTHLY) {
                if (updateRequest.getRentalValue() == null || updateRequest.getRentalValue().trim().isEmpty()) {
                    return new ApiResponse<>(false, "Rental value cannot be empty for " + updateRequest.getRentalType() + " rental type.", null);
                }
                existingLease.setRentalValue(updateRequest.getRentalValue());
            } else {
                existingLease.setRentalValue(null);
            }

            existingLease.setCommencementAmount(updateRequest.getCommencementAmount());

            Lease updatedLease = leaseRepository.save(existingLease);
            LeaseResponseDto responseDto = new LeaseResponseDto(updatedLease);

            return new ApiResponse<>(true, "Lease updated successfully.", responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error updating lease.", null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteLease(Long id) {
        try {
            Lease lease = leaseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lease not found with ID: " + id));

            // Check if lease has documents attached
            if (!lease.getDocuments().isEmpty()) {
                return new ApiResponse<>(false, "Cannot delete lease with attached documents. Please delete documents first.", null);
            }

            leaseRepository.delete(lease);
            return new ApiResponse<>(true, "Lease deleted successfully.", "Lease with ID " + id + " has been deleted.");
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error deleting lease.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getExpiringLeases(LocalDate startDate, LocalDate endDate) {
        try {
            List<Lease> expiringLeases = leaseRepository.findByExpiryDateBetween(startDate, endDate);
            List<LeaseResponseDto> responseDtos = expiringLeases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Expiring leases retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching expiring leases.", null);
        }
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    @Transactional
    public void processAutoRenewals() {
        try {
            LocalDate today = LocalDate.now();
            List<Lease> expiringLeases = leaseRepository.findByExpiryDateAndAutoRenewalOption(today, true);

            for (Lease lease : expiringLeases) {
                if (lease.isAutoRenewalOption() && lease.getRenewalPeriodMonths() != null) {
                    lease.autoRenew();
                    leaseRepository.save(lease);

                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesByStatus(String status) {
        try {
            LeaseStatus leaseStatus = LeaseStatus.valueOf(status.toUpperCase());
            List<Lease> leases = leaseRepository.findByStatus(leaseStatus);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases retrieved successfully for status: " + status, responseDtos);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, "Invalid status: " + status, null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by status.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesByRentalType(String rentalType) {
        try {
            RentalType type = RentalType.valueOf(rentalType.toUpperCase());
            List<Lease> leases = leaseRepository.findByRentalType(type);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases retrieved successfully for rental type: " + rentalType, responseDtos);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, "Invalid rental type: " + rentalType, null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by rental type.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesByLeaseType(String leaseType) {
        try {
            LeaseType type = LeaseType.valueOf(leaseType.toUpperCase());
            List<Lease> leases = leaseRepository.findByLeaseType(type);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases retrieved successfully for lease type: " + leaseType, responseDtos);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, "Invalid lease type: " + leaseType, null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by lease type.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getActiveLeases() {
        try {
            List<Lease> activeLeases = leaseRepository.findActiveLeases();
            List<LeaseResponseDto> responseDtos = activeLeases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Active leases retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching active leases.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesWithDocuments() {
        try {
            List<Lease> leases = leaseRepository.findLeasesWithDocuments();
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases with documents retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases with documents.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesWithoutDocuments() {
        try {
            List<Lease> leases = leaseRepository.findLeasesWithoutDocuments();
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases without documents retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases without documents.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesByAutoRenewalOption(boolean autoRenewal) {
        try {
            List<Lease> leases = leaseRepository.findByAutoRenewalOption(autoRenewal);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true,
                    "Leases with auto renewal " + (autoRenewal ? "enabled" : "disabled") + " retrieved successfully.",
                    responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by auto renewal option.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> searchLeases(String agreementNumber, String status, String operationalStatus, Long landlordId, Long siteId) {
        try {
            LeaseStatus leaseStatus = null;
            if (status != null) {
                leaseStatus = LeaseStatus.valueOf(status.toUpperCase());
            }

            OperationalStatus opStatus = null;
            if (operationalStatus != null) {
                opStatus = OperationalStatus.valueOf(operationalStatus.toUpperCase());
            }

            List<Lease> leases = leaseRepository.findByMultipleCriteria(agreementNumber, leaseStatus, opStatus, landlordId, siteId);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Search results retrieved successfully.", responseDtos);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, "Invalid status or operational status provided.", null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error searching leases.", null);
        }
    }

    @Override
    @Transactional
    public ApiResponse<LeaseResponseDto> addDocumentsToLease(Long leaseId, List<MultipartFile> files,
                                                             List<DocumentRequestDto> documentRequests) throws IOException {
        try {
            Lease lease = leaseRepository.findById(leaseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Lease not found with ID: " + leaseId));

            // STRICT VALIDATION: Only approved leases can have documents
            if (!lease.canAttachDocuments()) {
                return new ApiResponse<>(false,
                        "Documents can only be attached to approved leases. Current status: " + lease.getStatus(),
                        null);
            }

            if (files == null || files.isEmpty()) {
                return new ApiResponse<>(false, "No files provided for upload.", null);
            }

            if (files.size() != documentRequests.size()) {
                return new ApiResponse<>(false, "Number of files must match number of document requests.", null);
            }

            // Process each document
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                DocumentRequestDto documentRequest = documentRequests.get(i);

                Document document = createDocumentFromRequest(file, documentRequest, lease);
                lease.addDocument(document);

            }

            Lease updatedLease = leaseRepository.save(lease);
            LeaseResponseDto responseDto = new LeaseResponseDto(updatedLease);

            return new ApiResponse<>(true,
                    String.format("%d document(s) added to lease successfully.", files.size()),
                    responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error adding documents to lease.", null);
        }
    }

    private Document createDocumentFromRequest(MultipartFile file, DocumentRequestDto documentRequest, Lease lease) throws IOException {
        // Validate file type - only PDF allowed
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Invalid file path sequence.");
        }

        // Check file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB.");
        }

        // FIXED: fileUploadBaseDir is now available via @Value annotation
        Path uploadPath = Paths.get(fileUploadBaseDir, "documents");
        Files.createDirectories(uploadPath);

        String newFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path filePath = uploadPath.resolve(newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document();
        document.setFileName(newFileName);
        document.setFileUrl(filePath.toString());
        document.setDocumentType(documentRequest.getDocumentType());
        document.setCategory(documentRequest.getCategory());
        document.setDescription(documentRequest.getDescription());
        document.setUploadTime(LocalDateTime.now());
        document.setLease(lease);

        return document;
    }

    @Override
    public ApiResponse<Long> getTotalLeaseCount() {
        try {
            long count = leaseRepository.count();
            return new ApiResponse<>(true, "Total lease count retrieved successfully.", count);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching total lease count.", null);
        }
    }

    @Override
    public ApiResponse<Long> getLeaseCountByStatus(String status) {
        try {
            LeaseStatus leaseStatus = LeaseStatus.valueOf(status.toUpperCase());
            long count = leaseRepository.countByStatus(leaseStatus);
            return new ApiResponse<>(true, "Lease count for status " + status + " retrieved successfully.", count);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, "Invalid status: " + status, null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching lease count by status.", null);
        }
    }

    @Override
    public ApiResponse<Long> getLeaseCountByOperationalStatus(String operationalStatus) {
        try {
            OperationalStatus opStatus = OperationalStatus.valueOf(operationalStatus.toUpperCase());
            long count = leaseRepository.countByOperationalStatus(opStatus);
            return new ApiResponse<>(true, "Lease count for operational status " + operationalStatus + " retrieved successfully.", count);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, "Invalid operational status: " + operationalStatus, null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching lease count by operational status.", null);
        }
    }

    @Override
    public ApiResponse<LeaseResponseDto> uploadLeaseWithFile(MultipartFile file, LeaseRequestDto leaseRequestDto) throws IOException {
        // This method can be implemented if you want to support file upload during lease creation
        // For now, return not implemented
        return new ApiResponse<>(false, "Method not implemented.", null);
    }

    @Override
    public byte[] downloadLeaseFile(Long leaseId) throws IOException {
        // This method would need to be implemented if you want to download lease files
        // For now, return empty array
        return new byte[0];
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesByOperationalStatus(String operationalStatus) {
        try {
            OperationalStatus status = OperationalStatus.valueOf(operationalStatus.toUpperCase());
            List<Lease> leases = leaseRepository.findByOperationalStatus(status);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases retrieved successfully for operational status: " + operationalStatus, responseDtos);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, "Invalid operational status: " + operationalStatus, null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by operational status.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesByLandlordId(Long landlordId) {
        try {
            if (!landlordRepository.existsById(landlordId)) {
                return new ApiResponse<>(false, "Landlord not found with ID: " + landlordId, null);
            }
            List<Lease> leases = leaseRepository.findByLandlord_Id(landlordId);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases retrieved successfully for landlord.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by landlord ID.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesBySiteId(Long siteId) {
        try {
            if (!siteRepository.existsById(siteId)) {
                return new ApiResponse<>(false, "Site not found with ID: " + siteId, null);
            }
            List<Lease> leases = leaseRepository.findBySite_Id(siteId);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases retrieved successfully for site.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by site ID.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesByCategory(String category) {
        try {
            List<Lease> leases = leaseRepository.findByLeaseCategoryContainingIgnoreCase(category);
            List<LeaseResponseDto> responseDtos = leases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Leases retrieved successfully for category: " + category, responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by category.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getExpiredLeases() {
        try {
            LocalDate today = LocalDate.now();
            List<Lease> expiredLeases = leaseRepository.findByExpiryDateBefore(today);
            List<LeaseResponseDto> responseDtos = expiredLeases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Expired leases retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching expired leases.", null);
        }
    }

    @Override
    public ApiResponse<List<LeaseResponseDto>> getLeasesByCategoryAndStatus(String category, String status) {
        try {
            LeaseStatus leaseStatus = LeaseStatus.valueOf(status.toUpperCase());
            List<Lease> leases = leaseRepository.findByLeaseCategoryContainingIgnoreCase(category);
            // Filter by status in memory or create a new repository method
            List<Lease> filteredLeases = leases.stream()
                    .filter(lease -> lease.getStatus() == leaseStatus)
                    .collect(Collectors.toList());

            List<LeaseResponseDto> responseDtos = filteredLeases.stream()
                    .map(LeaseResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true,
                    "Leases retrieved successfully for category: " + category + " and status: " + status,
                    responseDtos);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, "Invalid status: " + status, null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching leases by category and status.", null);
        }
    }

    @Override
    public ApiResponse<Map<String, Object>> getLeaseStatistics() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate nextMonth = today.plusDays(30);
            Map<String, Object> statistics = new HashMap<>();

            // Total leases count
            statistics.put("totalLeases", leaseRepository.count());

            // Leases by status
            for (LeaseStatus status : LeaseStatus.values()) {
                statistics.put(status.name().toLowerCase() + "Leases", leaseRepository.countByStatus(status));
            }

            // Expired leases
            statistics.put("expiredLeases", leaseRepository.countByExpiryDateBefore(today));

            // Leases expiring soon (next 30 days)
            statistics.put("leasesExpiringSoon", leaseRepository.countByExpiryDateBetween(today, nextMonth));

            // Leases with auto renewal
            statistics.put("autoRenewalLeases", leaseRepository.countByAutoRenewalOption(true));

            return new ApiResponse<>(true, "Lease statistics retrieved successfully.", statistics);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching lease statistics.", null);
        }
    }

    @Override
    public ApiResponse<Map<String, Object>> getLandlordLeaseStatistics(Long landlordId) {
        try {
            if (!landlordRepository.existsById(landlordId)) {
                return new ApiResponse<>(false, "Landlord not found with ID: " + landlordId, null);
            }

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalLeases", leaseRepository.countByLandlord_Id(landlordId));

            for (LeaseStatus status : LeaseStatus.values()) {
                statistics.put(status.name().toLowerCase() + "Leases",
                        leaseRepository.countByLandlord_IdAndStatus(landlordId, status));
            }

            statistics.put("autoRenewalLeases",
                    leaseRepository.countByAutoRenewalOptionAndStatus(true, LeaseStatus.ACTIVE));

            return new ApiResponse<>(true, "Landlord lease statistics retrieved successfully.", statistics);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching landlord lease statistics.", null);
        }
    }

    @Override
    public ApiResponse<Map<String, Object>> getExpiryStatistics() {
        try {
            LocalDate today = LocalDate.now();
            Map<String, Object> statistics = new HashMap<>();

            // Expiry statistics for different time periods
            statistics.put("expired", leaseRepository.countByExpiryDateBefore(today));
            statistics.put("expiringThisWeek", leaseRepository.countByExpiryDateBetween(today, today.plusDays(7)));
            statistics.put("expiringThisMonth", leaseRepository.countByExpiryDateBetween(today, today.plusDays(30)));
            statistics.put("expiringNext3Months", leaseRepository.countByExpiryDateBetween(today, today.plusDays(90)));

            return new ApiResponse<>(true, "Expiry statistics retrieved successfully.", statistics);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching expiry statistics.", null);
        }
    }
}