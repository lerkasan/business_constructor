package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT count(u) = 0 FROM User u WHERE LOWER(u.email) = LOWER(?)")
    boolean emailAvailable(String email);

    @Query("SELECT count(u) = 0 FROM User u WHERE LOWER(u.username) = LOWER(?)")
    boolean usernameAvailable(String username);
}
