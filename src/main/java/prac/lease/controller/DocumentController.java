package prac.lease.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.DocumentRequestDto;
import prac.lease.dto.DocumentResponseDto;
import prac.lease.exception.ResourceNotFoundException;
import prac.lease.service.DocumentService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // ========== BASIC CRUD OPERATIONS ==========

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @ModelAttribute DocumentRequestDto documentRequestDto) throws IOException {
        ApiResponse<DocumentResponseDto> apiResponse = documentService.uploadDocument(file, documentRequestDto);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> getDocumentById(@PathVariable Long id) {
        ApiResponse<DocumentResponseDto> apiResponse = documentService.getDocumentById(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getAllDocuments() {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getAllDocuments();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentResponseDto>> updateDocument(
            @PathVariable Long id,
            @RequestBody DocumentRequestDto documentRequestDto) {
        ApiResponse<DocumentResponseDto> apiResponse = documentService.updateDocument(id, documentRequestDto);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDocument(@PathVariable Long id) {
        ApiResponse<String> apiResponse = documentService.deleteDocument(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    // ========== FILTERING OPERATIONS ==========

    @GetMapping("/lease/{leaseId}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getDocumentsByLeaseId(@PathVariable Long leaseId) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getDocumentsByLeaseId(leaseId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/document-type/{documentType}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getDocumentsByDocumentType(@PathVariable String documentType) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getDocumentsByDocumentType(documentType);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getDocumentsByCategory(@PathVariable String category) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getDocumentsByCategory(category);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/landlord/{landlordId}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getDocumentsByLandlordId(@PathVariable Long landlordId) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getDocumentsByLandlordId(landlordId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getDocumentsBySiteId(@PathVariable Long siteId) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getDocumentsBySiteId(siteId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    // ========== SEARCH OPERATIONS ==========

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> searchDocuments(
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String documentType,
            @RequestParam(required = false) String category) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.searchDocuments(fileName, documentType, category);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/uploaded-after/{date}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getDocumentsUploadedAfter(@PathVariable String date) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getDocumentsUploadedAfter(date);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/uploaded-before/{date}")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getDocumentsUploadedBefore(@PathVariable String date) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getDocumentsUploadedBefore(date);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> getLatestDocuments(
            @RequestParam(defaultValue = "10") int limit) {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.getLatestDocuments(limit);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ========== FILE OPERATIONS ==========

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getDocumentFileById(@PathVariable Long id) {
        try {
            byte[] fileData = documentService.getFileById(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(fileData.length);
            headers.setContentDispositionFormData("attachment", "document-" + id + ".pdf");

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/file-url")
    public ResponseEntity<ApiResponse<String>> getDocumentFileUrl(@PathVariable Long id) {
        ApiResponse<String> apiResponse = documentService.getFileUrl(id);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    // ========== VALIDATION OPERATIONS ==========

    @GetMapping("/validate-upload/{leaseId}")
    public ResponseEntity<ApiResponse<Boolean>> validateDocumentUpload(@PathVariable Long leaseId) {
        ApiResponse<Boolean> apiResponse = documentService.validateDocumentUpload(leaseId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // ========== STATISTICS OPERATIONS ==========

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDocumentStatistics() {
        ApiResponse<Map<String, Object>> apiResponse = documentService.getDocumentStatistics();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/statistics/lease/{leaseId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getLeaseDocumentStatistics(@PathVariable Long leaseId) {
        ApiResponse<Map<String, Object>> apiResponse = documentService.getLeaseDocumentStatistics(leaseId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/count/lease/{leaseId}")
    public ResponseEntity<ApiResponse<Long>> getDocumentCountByLeaseId(@PathVariable Long leaseId) {
        ApiResponse<Long> apiResponse = documentService.getDocumentCountByLeaseId(leaseId);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/count/document-type/{documentType}")
    public ResponseEntity<ApiResponse<Long>> getDocumentCountByType(@PathVariable String documentType) {
        ApiResponse<Long> apiResponse = documentService.getDocumentCountByType(documentType);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/count/category/{category}")
    public ResponseEntity<ApiResponse<Long>> getDocumentCountByCategory(@PathVariable String category) {
        ApiResponse<Long> apiResponse = documentService.getDocumentCountByCategory(category);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // ========== BULK OPERATIONS ==========

    @PostMapping("/upload-multiple")
    public ResponseEntity<ApiResponse<List<DocumentResponseDto>>> uploadMultipleDocuments(
            @RequestParam("files") MultipartFile[] files,
            @ModelAttribute DocumentRequestDto documentRequestDto) throws IOException {
        ApiResponse<List<DocumentResponseDto>> apiResponse = documentService.uploadMultipleDocuments(files, documentRequestDto);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<ApiResponse<String>> deleteMultipleDocuments(@RequestBody List<Long> documentIds) {
        ApiResponse<String> apiResponse = documentService.deleteMultipleDocuments(documentIds);
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    // ========== DOCUMENT TYPE MANAGEMENT ==========

    @GetMapping("/document-types")
    public ResponseEntity<ApiResponse<List<String>>> getAllDocumentTypes() {
        ApiResponse<List<String>> apiResponse = documentService.getAllDocumentTypes();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllCategories() {
        ApiResponse<List<String>> apiResponse = documentService.getAllCategories();
        return new ResponseEntity<>(apiResponse, apiResponse.success() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}