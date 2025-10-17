package prac.lease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prac.lease.model.Lease;
import prac.lease.model.LeaseStatus;
import prac.lease.model.OperationalStatus;
import prac.lease.model.RentalType;
import prac.lease.model.LeaseType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long> {

    // Check if agreement number already exists
    boolean existsByAgreementNumber(String agreementNumber);

    // Find leases by status
    List<Lease> findByStatus(LeaseStatus status);

    // Find leases by operational status
    List<Lease> findByOperationalStatus(OperationalStatus operationalStatus);

    // Find leases by landlord ID
    List<Lease> findByLandlord_Id(Long landlordId);

    // Find leases by site ID
    List<Lease> findBySite_Id(Long siteId);

    // Find leases expiring between dates
    List<Lease> findByExpiryDateBetween(LocalDate startDate, LocalDate endDate);

    // Find leases expiring on a specific date with auto-renewal option
    List<Lease> findByExpiryDateAndAutoRenewalOption(LocalDate expiryDate, boolean autoRenewalOption);

    // Find leases by rental type
    List<Lease> findByRentalType(RentalType rentalType);

    // Find leases by lease type
    List<Lease> findByLeaseType(LeaseType leaseType);

    // Find leases by auto renewal option
    List<Lease> findByAutoRenewalOption(boolean autoRenewalOption);

    // Find active leases (not expired or rejected)
    @Query("SELECT l FROM Lease l WHERE l.status NOT IN (prac.lease.model.LeaseStatus.EXPIRED, prac.lease.model.LeaseStatus.REJECTED)")
    List<Lease> findActiveLeases();

    // Find leases expiring soon (within next 30 days)
    @Query("SELECT l FROM Lease l WHERE l.expiryDate BETWEEN :startDate AND :endDate AND l.status NOT IN (prac.lease.model.LeaseStatus.EXPIRED, prac.lease.model.LeaseStatus.REJECTED)")
    List<Lease> findLeasesExpiringSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find leases by agreement number (exact match)
    Optional<Lease> findByAgreementNumber(String agreementNumber);

    // Find leases by agreement number containing (like search)
    List<Lease> findByAgreementNumberContainingIgnoreCase(String agreementNumber);

    // Count leases by status
    long countByStatus(LeaseStatus status);

    // Count leases by operational status
    long countByOperationalStatus(OperationalStatus operationalStatus);

    // Find leases with documents
    @Query("SELECT l FROM Lease l WHERE SIZE(l.documents) > 0")
    List<Lease> findLeasesWithDocuments();

    // Find leases without documents
    @Query("SELECT l FROM Lease l WHERE SIZE(l.documents) = 0")
    List<Lease> findLeasesWithoutDocuments();

    // Find leases by landlord and status
    List<Lease> findByLandlord_IdAndStatus(Long landlordId, LeaseStatus status);

    // Find leases by site and status
    List<Lease> findBySite_IdAndStatus(Long siteId, LeaseStatus status);

    // Find leases by commencement date range
    List<Lease> findByCommencementDateBetween(LocalDate startDate, LocalDate endDate);

    // Find leases that have expired but not yet processed
    @Query("SELECT l FROM Lease l WHERE l.expiryDate < :currentDate AND l.status NOT IN (prac.lease.model.LeaseStatus.EXPIRED, prac.lease.model.LeaseStatus.REJECTED)")
    List<Lease> findExpiredLeasesNotProcessed(@Param("currentDate") LocalDate currentDate);

    // Find leases by multiple criteria
    @Query("SELECT l FROM Lease l WHERE " +
            "(:agreementNumber IS NULL OR l.agreementNumber LIKE %:agreementNumber%) AND " +
            "(:status IS NULL OR l.status = :status) AND " +
            "(:operationalStatus IS NULL OR l.operationalStatus = :operationalStatus) AND " +
            "(:landlordId IS NULL OR l.landlord.id = :landlordId) AND " +
            "(:siteId IS NULL OR l.site.id = :siteId)")
    List<Lease> findByMultipleCriteria(@Param("agreementNumber") String agreementNumber,
                                       @Param("status") LeaseStatus status,
                                       @Param("operationalStatus") OperationalStatus operationalStatus,
                                       @Param("landlordId") Long landlordId,
                                       @Param("siteId") Long siteId);

    // NEW: Find leases by lease category containing (case insensitive)
    List<Lease> findByLeaseCategoryContainingIgnoreCase(String leaseCategory);

    // NEW: Find leases by exact lease category
    List<Lease> findByLeaseCategory(String leaseCategory);

    // NEW: Find expired leases (expiry date before given date)
    List<Lease> findByExpiryDateBefore(LocalDate date);

    // NEW: Find leases expiring after given date
    List<Lease> findByExpiryDateAfter(LocalDate date);

    // NEW: Find leases by commencement date before given date
    List<Lease> findByCommencementDateBefore(LocalDate date);

    // NEW: Find leases by commencement date after given date
    List<Lease> findByCommencementDateAfter(LocalDate date);

    // NEW: Find leases by status and operational status
    List<Lease> findByStatusAndOperationalStatus(LeaseStatus status, OperationalStatus operationalStatus);

    // NEW: Find leases by auto renewal option and status
    List<Lease> findByAutoRenewalOptionAndStatus(boolean autoRenewalOption, LeaseStatus status);

    // NEW: Find leases by landlord ID and operational status
    List<Lease> findByLandlord_IdAndOperationalStatus(Long landlordId, OperationalStatus operationalStatus);

    // NEW: Find leases by site ID and operational status
    List<Lease> findBySite_IdAndOperationalStatus(Long siteId, OperationalStatus operationalStatus);

    // NEW: Count leases by lease category
    long countByLeaseCategory(String leaseCategory);

    // NEW: Find leases by rental value range (if rental value is numeric)
    @Query("SELECT l FROM Lease l WHERE l.rentalValue IS NOT NULL AND CAST(l.rentalValue AS double) BETWEEN :minValue AND :maxValue")
    List<Lease> findByRentalValueBetween(@Param("minValue") Double minValue, @Param("maxValue") Double maxValue);

    // NEW: Find leases with documents count greater than
    @Query("SELECT l FROM Lease l WHERE SIZE(l.documents) > :minDocumentCount")
    List<Lease> findLeasesWithDocumentCountGreaterThan(@Param("minDocumentCount") int minDocumentCount);

    // ========== MISSING COUNT METHODS ==========

    // Count leases by auto renewal option
    long countByAutoRenewalOption(boolean autoRenewalOption);

    // Count leases by rental type
    long countByRentalType(RentalType rentalType);

    // Count leases by lease type
    long countByLeaseType(LeaseType leaseType);

    // Count leases by landlord ID
    long countByLandlord_Id(Long landlordId);

    // Count leases by site ID
    long countBySite_Id(Long siteId);

    // Count leases expiring before given date
    long countByExpiryDateBefore(LocalDate date);

    // Count leases expiring after given date
    long countByExpiryDateAfter(LocalDate date);

    // Count leases expiring between dates
    long countByExpiryDateBetween(LocalDate startDate, LocalDate endDate);

    // Count leases by commencement date before given date
    long countByCommencementDateBefore(LocalDate date);

    // Count leases by commencement date after given date
    long countByCommencementDateAfter(LocalDate date);

    // Count leases by commencement date between dates
    long countByCommencementDateBetween(LocalDate startDate, LocalDate endDate);

    // Count leases with documents
    @Query("SELECT COUNT(l) FROM Lease l WHERE SIZE(l.documents) > 0")
    long countLeasesWithDocuments();

    // Count leases without documents
    @Query("SELECT COUNT(l) FROM Lease l WHERE SIZE(l.documents) = 0")
    long countLeasesWithoutDocuments();

    // Count leases by status and operational status
    long countByStatusAndOperationalStatus(LeaseStatus status, OperationalStatus operationalStatus);

    // Count leases by auto renewal option and status
    long countByAutoRenewalOptionAndStatus(boolean autoRenewalOption, LeaseStatus status);

    // Count leases by landlord ID and status
    long countByLandlord_IdAndStatus(Long landlordId, LeaseStatus status);

    // Count leases by site ID and status
    long countBySite_IdAndStatus(Long siteId, LeaseStatus status);

    // ========== ADVANCED STATISTICS METHODS ==========

    // Get lease count grouped by status
    @Query("SELECT l.status, COUNT(l) FROM Lease l GROUP BY l.status")
    List<Object[]> countLeasesGroupedByStatus();

    // Get lease count grouped by operational status
    @Query("SELECT l.operationalStatus, COUNT(l) FROM Lease l GROUP BY l.operationalStatus")
    List<Object[]> countLeasesGroupedByOperationalStatus();

    // Get lease count grouped by lease type
    @Query("SELECT l.leaseType, COUNT(l) FROM Lease l GROUP BY l.leaseType")
    List<Object[]> countLeasesGroupedByLeaseType();

    // Get lease count grouped by rental type
    @Query("SELECT l.rentalType, COUNT(l) FROM Lease l GROUP BY l.rentalType")
    List<Object[]> countLeasesGroupedByRentalType();

    // Get lease count grouped by lease category
    @Query("SELECT l.leaseCategory, COUNT(l) FROM Lease l WHERE l.leaseCategory IS NOT NULL GROUP BY l.leaseCategory")
    List<Object[]> countLeasesGroupedByLeaseCategory();

    // Get average renewal period for auto-renewal leases
    @Query("SELECT AVG(l.renewalPeriodMonths) FROM Lease l WHERE l.autoRenewalOption = true AND l.renewalPeriodMonths IS NOT NULL")
    Double findAverageRenewalPeriod();

    // Get leases that will expire in the next X days
    @Query("SELECT COUNT(l) FROM Lease l WHERE l.expiryDate BETWEEN :startDate AND :endDate AND l.status NOT IN (prac.lease.model.LeaseStatus.EXPIRED, prac.lease.model.LeaseStatus.REJECTED)")
    long countLeasesExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}