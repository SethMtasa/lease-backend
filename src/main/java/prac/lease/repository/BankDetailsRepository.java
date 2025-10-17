package prac.lease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prac.lease.model.BankDetails;

import java.util.List;

@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {

    List<BankDetails> findByLandlordId(Long landlordId);

    boolean existsByAccountNumberAndSortCode(String accountNumber, String sortCode);

    boolean existsByAccountNumberAndSortCodeAndIdNot(String accountNumber, String sortCode, Long id);

    List<BankDetails> findByBankContainingIgnoreCase(String bankName);

    List<BankDetails> findByAccountNumberContaining(String accountNumber);
}