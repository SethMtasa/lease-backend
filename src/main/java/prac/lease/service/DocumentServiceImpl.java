package prac.lease.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import prac.lease.dto.ApiResponse;
import prac.lease.dto.DocumentRequestDto;
import prac.lease.dto.DocumentResponseDto;
import prac.lease.exception.ResourceNotFoundException;
import prac.lease.model.Document;
import prac.lease.model.Lease;
import prac.lease.repository.DocumentRepository;
import prac.lease.repository.LeaseRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final LeaseRepository leaseRepository;

    @Value("${file.upload.path}")
    private String fileUploadBaseDir;

    public DocumentServiceImpl(DocumentRepository documentRepository, LeaseRepository leaseRepository) {
        this.documentRepository = documentRepository;
        this.leaseRepository = leaseRepository;
    }

    @Override
    @Transactional
    public ApiResponse<DocumentResponseDto> uploadDocument(MultipartFile file, DocumentRequestDto documentRequestDto) throws IOException {
        try {
            Lease lease = leaseRepository.findById(documentRequestDto.getLeaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lease not found with ID: " + documentRequestDto.getLeaseId()));

            // Check if lease is approved
            if (!lease.canAttachDocuments()) {
                return new ApiResponse<>(false,
                        "Documents can only be attached to approved leases. Current status: " + lease.getStatus(),
                        null);
            }

            // Validate file type - only PDF allowed
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".pdf")) {
                return new ApiResponse<>(false, "Only PDF files are allowed.", null);
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (fileName.contains("..")) {
                return new ApiResponse<>(false, "Invalid file path sequence.", null);
            }

            // Check file size (max 10MB)
            if (file.getSize() > 10 * 1024 * 1024) {
                return new ApiResponse<>(false, "File size must be less than 10MB.", null);
            }

            Path uploadPath = Paths.get(fileUploadBaseDir, "documents");
            Files.createDirectories(uploadPath);

            String newFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Document newDocument = new Document();
            newDocument.setFileName(newFileName);
            newDocument.setFileUrl(filePath.toString());
            newDocument.setDocumentType(documentRequestDto.getDocumentType());
            newDocument.setCategory(documentRequestDto.getCategory());
            newDocument.setDescription(documentRequestDto.getDescription());
            newDocument.setUploadTime(LocalDateTime.now());
            newDocument.setLease(lease);

            Document savedDocument = documentRepository.save(newDocument);
            DocumentResponseDto responseDto = new DocumentResponseDto(savedDocument);

            return new ApiResponse<>(true, "Document uploaded successfully.", responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (IOException e) {
            return new ApiResponse<>(false, "Error saving uploaded document file.", null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error uploading document.", null);
        }
    }

    @Override
    public ApiResponse<DocumentResponseDto> getDocumentById(Long id) {
        try {
            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));
            DocumentResponseDto responseDto = new DocumentResponseDto(document);
            return new ApiResponse<>(true, "Document retrieved successfully.", responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching document.", null);
        }
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getAllDocuments() {
        try {
            List<Document> documents = documentRepository.findAll();
            List<DocumentResponseDto> responseDtos = documents.stream()
                    .map(DocumentResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "All documents retrieved successfully.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching all documents.", null);
        }
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getDocumentsByLeaseId(Long leaseId) {
        try {
            if (!leaseRepository.existsById(leaseId)) {
                return new ApiResponse<>(false, "Lease not found with ID: " + leaseId, null);
            }
            List<Document> documents = documentRepository.findByLease_Id(leaseId);
            List<DocumentResponseDto> responseDtos = documents.stream()
                    .map(DocumentResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Documents retrieved successfully for lease.", responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching documents.", null);
        }
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getDocumentsByDocumentType(String documentType) {
        try {
            List<Document> documents = documentRepository.findByDocumentType(documentType);
            List<DocumentResponseDto> responseDtos = documents.stream()
                    .map(DocumentResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Documents retrieved successfully for document type: " + documentType, responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching documents by document type.", null);
        }
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getDocumentsByCategory(String category) {
        try {
            List<Document> documents = documentRepository.findByCategory(category);
            List<DocumentResponseDto> responseDtos = documents.stream()
                    .map(DocumentResponseDto::new)
                    .collect(Collectors.toList());
            return new ApiResponse<>(true, "Documents retrieved successfully for category: " + category, responseDtos);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error fetching documents by category.", null);
        }
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getDocumentsByLandlordId(Long landlordId) {
        return null;
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getDocumentsBySiteId(Long siteId) {
        return null;
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> searchDocuments(String fileName, String documentType, String category) {
        return null;
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getDocumentsUploadedAfter(String date) {
        return null;
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getDocumentsUploadedBefore(String date) {
        return null;
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> getLatestDocuments(int limit) {
        return null;
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteDocument(Long id) {
        try {
            Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));

            // Delete physical file
            Path filePath = Paths.get(document.getFileUrl());
            Files.deleteIfExists(filePath);

            documentRepository.delete(document);
            return new ApiResponse<>(true, "Document deleted successfully.", "Document with ID " + id + " has been deleted.");
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (IOException e) {

            return new ApiResponse<>(false, "Error deleting document file from storage.", null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error deleting document.", null);
        }
    }

    @Override
    public byte[] getFileById(Long id) throws IOException {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));

        String filePathString = document.getFileUrl();
        if (filePathString == null || filePathString.isEmpty()) {
            throw new ResourceNotFoundException("No file associated with document ID: " + id);
        }

        Path filePath = Paths.get(filePathString);
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new IOException("File not found or is not readable: " + filePathString);
        }

        return Files.readAllBytes(filePath);
    }

    @Override
    public ApiResponse<String> getFileUrl(Long id) {
        return null;
    }

    @Override
    public ApiResponse<Boolean> validateDocumentUpload(Long leaseId) {
        return null;
    }

    @Override
    public ApiResponse<Map<String, Object>> getDocumentStatistics() {
        return null;
    }

    @Override
    public ApiResponse<Map<String, Object>> getLeaseDocumentStatistics(Long leaseId) {
        return null;
    }

    @Override
    public ApiResponse<Long> getDocumentCountByLeaseId(Long leaseId) {
        return null;
    }

    @Override
    public ApiResponse<Long> getDocumentCountByType(String documentType) {
        return null;
    }

    @Override
    public ApiResponse<Long> getDocumentCountByCategory(String category) {
        return null;
    }

    @Override
    public ApiResponse<List<DocumentResponseDto>> uploadMultipleDocuments(MultipartFile[] files, DocumentRequestDto documentRequestDto) throws IOException {
        return null;
    }

    @Override
    public ApiResponse<String> deleteMultipleDocuments(List<Long> documentIds) {
        return null;
    }

    @Override
    public ApiResponse<List<String>> getAllDocumentTypes() {
        return null;
    }

    @Override
    public ApiResponse<List<String>> getAllCategories() {
        return null;
    }

    @Override
    @Transactional
    public ApiResponse<DocumentResponseDto> updateDocument(Long id, DocumentRequestDto documentRequestDto) {
        try {
            Document existingDocument = documentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + id));

            // Update only metadata, not the file itself
            existingDocument.setDocumentType(documentRequestDto.getDocumentType());
            existingDocument.setCategory(documentRequestDto.getCategory());
            existingDocument.setDescription(documentRequestDto.getDescription());

            Document updatedDocument = documentRepository.save(existingDocument);
            DocumentResponseDto responseDto = new DocumentResponseDto(updatedDocument);

            return new ApiResponse<>(true, "Document metadata updated successfully.", responseDto);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {

            return new ApiResponse<>(false, "Error updating document.", null);
        }
    }
}