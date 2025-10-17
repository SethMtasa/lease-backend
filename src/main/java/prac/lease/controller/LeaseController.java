package prac.lease.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.DocumentRequestDto;
import prac.lease.dto.LeaseRequestDto;
import prac.lease.dto.LeaseResponseDto;
import prac.lease.service.LeaseService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/leases")
public class LeaseController {

    private final LeaseService leaseService;

    public LeaseController(LeaseService leaseService) {
        this.leaseService = leaseService;
    }

    // ========== BASIC CRUD OPERATIONS ==========

    @PostMapping
    public ResponseEntity<ApiResponse<LeaseResponseDto>> createLease(@RequestBody LeaseRequestDto leaseRequest) {
        ApiResponse<LeaseResponseDto> apiResponse = leaseService.createLease(leaseRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaseResponseDto>> getLeaseById(@PathVariable Long id) {
        ApiResponse<LeaseResponseDto> apiResponse = leaseService.getLeaseById(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getAllLeases() {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getAllLeases();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaseResponseDto>> updateLease(@PathVariable Long id, @RequestBody LeaseRequestDto updateRequest) {
        ApiResponse<LeaseResponseDto> apiResponse = leaseService.updateLease(id, updateRequest);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteLease(@PathVariable Long id) {
        ApiResponse<String> apiResponse = leaseService.deleteLease(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    // ========== APPROVAL WORKFLOW OPERATIONS ==========

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<LeaseResponseDto>> approveLease(@PathVariable Long id) {
        ApiResponse<LeaseResponseDto> apiResponse = leaseService.approveLease(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<LeaseResponseDto>> rejectLease(@PathVariable Long id, @RequestParam String reason) {
        ApiResponse<LeaseResponseDto> apiResponse = leaseService.rejectLease(id, reason);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/pending-approval")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getPendingApprovalLeases() {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getPendingApprovalLeases();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/approved")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getApprovedLeases() {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getApprovedLeases();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========== FILTERING AND SEARCH OPERATIONS ==========

    @GetMapping("/operational-status/{operationalStatus}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByOperationalStatus(@PathVariable String operationalStatus) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesByOperationalStatus(operationalStatus);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByLandlordId(@PathVariable Long landlordId) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesByLandlordId(landlordId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesBySiteId(@PathVariable Long siteId) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesBySiteId(siteId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByCategory(@PathVariable String category) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesByCategory(category);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/category/{category}/status/{status}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByCategoryAndStatus(
            @PathVariable String category, @PathVariable String status) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesByCategoryAndStatus(category, status);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/expiring")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getExpiringLeases(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getExpiringLeases(startDate, endDate);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/expired")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getExpiredLeases() {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getExpiredLeases();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========== STATUS FILTERING ==========

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByStatus(@PathVariable String status) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesByStatus(status);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/rental-type/{rentalType}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByRentalType(@PathVariable String rentalType) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesByRentalType(rentalType);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/lease-type/{leaseType}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByLeaseType(@PathVariable String leaseType) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesByLeaseType(leaseType);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getActiveLeases() {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getActiveLeases();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/with-documents")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesWithDocuments() {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesWithDocuments();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/without-documents")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesWithoutDocuments() {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesWithoutDocuments();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/auto-renewal/{autoRenewal}")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> getLeasesByAutoRenewalOption(@PathVariable boolean autoRenewal) {
        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.getLeasesByAutoRenewalOption(autoRenewal);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========== STATISTICS AND REPORTING ==========

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getLeaseStatistics() {
        ApiResponse<Map<String, Object>> apiResponse = leaseService.getLeaseStatistics();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/statistics/landlord/{landlordId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getLandlordLeaseStatistics(@PathVariable Long landlordId) {
        ApiResponse<Map<String, Object>> apiResponse = leaseService.getLandlordLeaseStatistics(landlordId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/statistics/expiry")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getExpiryStatistics() {
        ApiResponse<Map<String, Object>> apiResponse = leaseService.getExpiryStatistics();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========== COUNT OPERATIONS ==========

    @GetMapping("/count/total")
    public ResponseEntity<ApiResponse<Long>> getTotalLeaseCount() {
        ApiResponse<Long> apiResponse = leaseService.getTotalLeaseCount();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<ApiResponse<Long>> getLeaseCountByStatus(@PathVariable String status) {
        ApiResponse<Long> apiResponse = leaseService.getLeaseCountByStatus(status);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/count/operational-status/{operationalStatus}")
    public ResponseEntity<ApiResponse<Long>> getLeaseCountByOperationalStatus(@PathVariable String operationalStatus) {
        ApiResponse<Long> apiResponse = leaseService.getLeaseCountByOperationalStatus(operationalStatus);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // ========== ADVANCED SEARCH ==========

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<LeaseResponseDto>>> searchLeases(
            @RequestParam(required = false) String agreementNumber,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String operationalStatus,
            @RequestParam(required = false) Long landlordId,
            @RequestParam(required = false) Long siteId) {

        ApiResponse<List<LeaseResponseDto>> apiResponse = leaseService.searchLeases(
                agreementNumber, status, operationalStatus, landlordId, siteId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // ========== FILE OPERATIONS ==========

    @PostMapping("/upload-with-file")
    public ResponseEntity<ApiResponse<LeaseResponseDto>> uploadLeaseWithFile(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute LeaseRequestDto leaseRequestDto) throws IOException {
        ApiResponse<LeaseResponseDto> apiResponse = leaseService.uploadLeaseWithFile(file, leaseRequestDto);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

//    @GetMapping("/{id}/download-file")
//    public ResponseEntity<byte[]> downloadLeaseFile(@PathVariable Long id) throws IOException {
//        byte[] fileData = leaseService.downloadLeaseFile(id);
//
//        // Set headers for file download
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("attachment", "lease-document-" + id + ".pdf");
//
//        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
//    }
//
//    // SEPARATE: Add documents to approved lease
//    @PostMapping("/{id}/documents")
//    public ResponseEntity<ApiResponse<LeaseResponseDto>> addDocumentsToLease(
//            @PathVariable Long id,
//            @RequestParam("files") List<MultipartFile> files,
//            @RequestParam("documentRequests") List<DocumentRequestDto> documentRequests) throws IOException {
//
//        ApiResponse<LeaseResponseDto> apiResponse = leaseService.addDocumentsToLease(id, files, documentRequests);
//        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
//    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<LeaseResponseDto>> addDocumentsToLease(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("documentRequests") String documentRequestsJson) throws IOException {

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert the JSON string to List<DocumentRequestDto>
        List<DocumentRequestDto> documentRequests = objectMapper.readValue(
                documentRequestsJson,
                new TypeReference<List<DocumentRequestDto>>() {}
        );

        ApiResponse<LeaseResponseDto> apiResponse = leaseService.addDocumentsToLease(id, files, documentRequests);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}