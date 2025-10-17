package prac.lease.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prac.lease.model.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
}