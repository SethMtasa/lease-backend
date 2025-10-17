package prac.lease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prac.lease.model.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Find documents by lease ID
    List<Document> findByLease_Id(Long leaseId);

    // Find documents by document type
    List<Document> findByDocumentType(String documentType);

    // Find documents by category
    List<Document> findByCategory(String category);

    // Find documents by upload time range
    List<Document> findByUploadTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find documents by lease ID and document type
    List<Document> findByLease_IdAndDocumentType(Long leaseId, String documentType);

    // Find documents by lease ID and category
    List<Document> findByLease_IdAndCategory(Long leaseId, String category);

    // Find documents containing filename
    List<Document> findByFileNameContainingIgnoreCase(String fileName);

    // Count documents by lease ID
    long countByLease_Id(Long leaseId);

    // Count documents by document type
    long countByDocumentType(String documentType);

    // Count documents by category
    long countByCategory(String category);

    // Find latest documents with limit
    List<Document> findTop10ByOrderByUploadTimeDesc();

    // Find documents by multiple criteria
    @Query("SELECT d FROM Document d WHERE " +
            "(:leaseId IS NULL OR d.lease.id = :leaseId) AND " +
            "(:documentType IS NULL OR d.documentType = :documentType) AND " +
            "(:category IS NULL OR d.category = :category) AND " +
            "(:fileName IS NULL OR LOWER(d.fileName) LIKE LOWER(CONCAT('%', :fileName, '%')))")
    List<Document> findByMultipleCriteria(@Param("leaseId") Long leaseId,
                                          @Param("documentType") String documentType,
                                          @Param("category") String category,
                                          @Param("fileName") String fileName);

    // Find documents with lease information (eager loading)
    @Query("SELECT d FROM Document d JOIN FETCH d.lease WHERE d.id = :id")
    Optional<Document> findByIdWithLease(@Param("id") Long id);

    // Find all documents with lease information
    @Query("SELECT d FROM Document d JOIN FETCH d.lease")
    List<Document> findAllWithLease();

    // Find documents by lease status
    @Query("SELECT d FROM Document d WHERE d.lease.status = :leaseStatus")
    List<Document> findByLeaseStatus(@Param("leaseStatus") String leaseStatus);

    // Find documents by landlord ID
    @Query("SELECT d FROM Document d WHERE d.lease.landlord.id = :landlordId")
    List<Document> findByLandlordId(@Param("landlordId") Long landlordId);

    // Find documents by site ID
    @Query("SELECT d FROM Document d WHERE d.lease.site.id = :siteId")
    List<Document> findBySiteId(@Param("siteId") Long siteId);

    // Find documents uploaded after a specific date
    List<Document> findByUploadTimeAfter(LocalDateTime dateTime);

    // Find documents uploaded before a specific date
    List<Document> findByUploadTimeBefore(LocalDateTime dateTime);

    // Check if document exists by filename
    boolean existsByFileName(String fileName);

    // Find documents by description containing text
    List<Document> findByDescriptionContainingIgnoreCase(String description);

    // Get document count by lease ID grouped by document type
    @Query("SELECT d.documentType, COUNT(d) FROM Document d WHERE d.lease.id = :leaseId GROUP BY d.documentType")
    List<Object[]> countDocumentsByTypeForLease(@Param("leaseId") Long leaseId);

    // Find duplicate documents (same filename and lease)
    @Query("SELECT d FROM Document d WHERE d.fileName = :fileName AND d.lease.id = :leaseId")
    List<Document> findDuplicateDocuments(@Param("fileName") String fileName, @Param("leaseId") Long leaseId);
}