package prac.lease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prac.lease.model.Landlord;
import java.util.List;
import java.util.Optional;

@Repository
public interface LandlordRepository extends JpaRepository<Landlord, Long> {

    // Find a landlord by their full name
    Optional<Landlord> findByFullName(String fullName);

    // Find all landlords in a specific region and district
//    List<Landlord> findByRegionAndDistrict(String region, String district);
}