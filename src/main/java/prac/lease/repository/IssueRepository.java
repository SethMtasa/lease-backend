package prac.lease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prac.lease.model.Issue;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    // Find all issues by a specific landlord
    List<Issue> findByLandlordId(Long landlordId);

    // Find all issues with a specific status (e.g., "open", "resolved")
    List<Issue> findByStatus(String status);
}