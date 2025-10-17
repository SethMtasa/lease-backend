package prac.lease.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prac.lease.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByUsername(String username);

        Optional<User> findByEmail(String email);

        Optional<User> findByUsernameAndActiveStatus(String username, boolean activeStatus);
        List<User> findByActiveStatus(boolean activeStatus);
}