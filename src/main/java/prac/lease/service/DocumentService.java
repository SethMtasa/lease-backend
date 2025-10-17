package prac.lease.service;

import org.springframework.web.multipart.MultipartFile;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.DocumentRequestDto;
import prac.lease.dto.DocumentResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DocumentService {

    // Basic CRUD operations
    ApiResponse<DocumentResponseDto> uploadDocument(MultipartFile file, DocumentRequestDto documentRequestDto) throws IOException;

    ApiResponse<DocumentResponseDto> getDocumentById(Long id);

    ApiResponse<List<DocumentResponseDto>> getAllDocuments();

    ApiResponse<DocumentResponseDto> updateDocument(Long id, DocumentRequestDto documentRequestDto);

    ApiResponse<String> deleteDocument(Long id);

    // Filtering operations
    ApiResponse<List<DocumentResponseDto>> getDocumentsByLeaseId(Long leaseId);

    ApiResponse<List<DocumentResponseDto>> getDocumentsByDocumentType(String documentType);

    ApiResponse<List<DocumentResponseDto>> getDocumentsByCategory(String category);

    ApiResponse<List<DocumentResponseDto>> getDocumentsByLandlordId(Long landlordId);

    ApiResponse<List<DocumentResponseDto>> getDocumentsBySiteId(Long siteId);

    // Search operations
    ApiResponse<List<DocumentResponseDto>> searchDocuments(String fileName, String documentType, String category);

    ApiResponse<List<DocumentResponseDto>> getDocumentsUploadedAfter(String date);

    ApiResponse<List<DocumentResponseDto>> getDocumentsUploadedBefore(String date);

    ApiResponse<List<DocumentResponseDto>> getLatestDocuments(int limit);

    // File operations
    byte[] getFileById(Long id) throws IOException;

    ApiResponse<String> getFileUrl(Long id);

    // Validation operations
    ApiResponse<Boolean> validateDocumentUpload(Long leaseId);

    // Statistics operations
    ApiResponse<Map<String, Object>> getDocumentStatistics();

    ApiResponse<Map<String, Object>> getLeaseDocumentStatistics(Long leaseId);

    ApiResponse<Long> getDocumentCountByLeaseId(Long leaseId);

    ApiResponse<Long> getDocumentCountByType(String documentType);

    ApiResponse<Long> getDocumentCountByCategory(String category);

    // Bulk operations
    ApiResponse<List<DocumentResponseDto>> uploadMultipleDocuments(MultipartFile[] files, DocumentRequestDto documentRequestDto) throws IOException;

    ApiResponse<String> deleteMultipleDocuments(List<Long> documentIds);

    // Document type management
    ApiResponse<List<String>> getAllDocumentTypes();

    ApiResponse<List<String>> getAllCategories();
}